package org.hexworks.microcline.adapters

import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawMode
import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.events.LayerCleared
import org.hexworks.microcline.events.LayerCreated
import org.hexworks.microcline.events.LayerDeleted
import org.hexworks.microcline.events.LayerMovedDown
import org.hexworks.microcline.events.LayerMovedUp
import org.hexworks.microcline.events.LayerSelected
import org.hexworks.microcline.events.LayerVisibilityChanged
import org.hexworks.microcline.extensions.DrawComponent
import org.hexworks.microcline.extensions.DrawLayersArea
import org.hexworks.microcline.extensions.onAnyMouseEvent
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.shape.LineFactory
import org.hexworks.zircon.api.uievent.MouseEvent
import org.hexworks.zircon.api.uievent.MouseEventProcessor
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_DRAGGED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_MOVED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_PRESSED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_RELEASED
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.UIEventPhase
import org.hexworks.zircon.internal.Zircon

/**
 * Transforms the [MouseEvent]s on the UI to the corresponding
 * draw commands and applies them to the given draw area using
 * the given [drawing].
 */
class DrawAdapter(
        private val drawLayersArea: DrawLayersArea,
        private val drawComponent: DrawComponent,
        private val drawing: Drawing,
        selectedModeProperty: Property<DrawMode>) : MouseEventProcessor {

    private val selectedMode: DrawMode by selectedModeProperty.asDelegate()

    private var undoDiff = mutableMapOf<Position, Tile>()
    private val touchedPositions = mutableListOf<Position>()
    private var startPos = Maybe.empty<Position>()
    private var currPos = Maybe.empty<Position>()
    private var selectedLayerIdx = 0

    private val drawLayers: List<Layer>
        get() = drawLayersArea.getOverlaysAt(0).toList()
    private val selectedLayer: Layer
        get() = drawLayers[selectedLayerIdx]

    init {
        drawComponent.onAnyMouseEvent(this)
        drawing.state.layers.forEach { layer ->
            createLayerFrom(layer)
        }
        Zircon.eventBus.subscribe<LayerMovedUp> {
            val oldTop = drawLayers[it.newIndex]
            val newTop = drawLayers[it.newIndex + 1]
            val temp = oldTop.toTileImage()
            oldTop.draw(newTop)
            newTop.clear()
            newTop.draw(temp)
        }
        Zircon.eventBus.subscribe<LayerMovedDown> {
            val oldBot = drawLayers[it.newIndex]
            val newBot = drawLayers[it.newIndex - 1]
            val temp = oldBot.toTileImage()
            oldBot.draw(newBot)
            newBot.clear()
            newBot.draw(temp)
        }
        Zircon.eventBus.subscribe<LayerVisibilityChanged> {
            drawLayers[it.index].isHidden = !it.visible
        }
        Zircon.eventBus.subscribe<LayerCleared> {
            drawLayers[it.index].clear()
        }
        Zircon.eventBus.subscribe<LayerDeleted> {
            drawLayersArea.removeOverlay(drawLayers[it.index], 0)
        }
        Zircon.eventBus.subscribe<LayerCreated> {
            createLayerFrom(it.layer)
        }
        Zircon.eventBus.subscribe<LayerSelected> {
            selectedLayerIdx = it.idx
        }
    }

    override fun process(event: MouseEvent, phase: UIEventPhase) {
        val position = event.position - drawComponent.contentPosition - drawComponent.absolutePosition
        when (event.type) {
            MOUSE_MOVED -> mouseMoved(position)
            MOUSE_PRESSED -> mousePressed(position)
            MOUSE_DRAGGED -> mouseDragged(position)
            MOUSE_RELEASED -> mouseReleased(position)
            else -> {
                Pass
            }
        }
    }

    private fun mouseMoved(position: Position) {
        val prevPos = currPos.orElse(position)
        currPos = Maybe.of(position)
        if (prevPos != position) {
            removeHighlight(prevPos)
            highlightPosition(position)
        }
    }

    private fun mousePressed(position: Position) {
        if (drawing.isNotLocked) {
            currPos.map(::removeHighlight)
            startPos = Maybe.of(position)
            currPos = Maybe.of(position)
            touchedPositions.add(position)
            updateDrawing()
        }
    }

    private fun mouseDragged(position: Position) {
        if (drawing.isNotLocked) {
            currPos.map { prevPos ->
                if (prevPos != position) {
                    currPos = Maybe.of(position)
                    touchedPositions.addAll(LineFactory.buildLine(prevPos, position).positions())
                    updateDrawing(true)
                }
            }
        }
    }

    private fun mouseReleased(position: Position) {
        if (drawing.isNotLocked) {
            updateDrawing()
            highlightPosition(position)
            resetEditingState()
            undoDiff.clear()
        }
    }

    private fun resetEditingState() {
        startPos = Maybe.empty()
        touchedPositions.clear()
    }

    private fun updateDrawing(withUndo: Boolean = false) {
        applyDiff(undoDiff)
        applyDiff(drawing.execute(selectedMode.asCommand(touchedPositions.toList())))
        val prevState = drawing.state
        if (withUndo) {
            drawing.undo()
            undoDiff.clear()
            undoDiff.putAll(drawing.state.diff(prevState))
        }
    }

    private fun applyDiff(diff: Map<Position, Tile>) {
        diff.forEach { (pos, tile) ->
            selectedLayer.setTileAt(pos, tile)
        }
    }

    private fun removeHighlight(prevMousePosition: Position) {
        selectedLayer.getTileAt(prevMousePosition).map {
            selectedLayer.setTileAt(prevMousePosition, it.withNoModifiers())
        }
    }

    private fun highlightPosition(position: Position) {
        selectedLayer.setTileAt(position, selectedLayer.getTileAt(position)
                .orElse(Tiles.empty())
                .withModifiers(Modifiers.border()))
    }

    private fun createLayerFrom(layer: DrawLayer) {
        drawLayersArea.pushOverlayAt(Layers.newBuilder()
                .withSize(Config.DRAWING_SIZE)
                .build().apply {
                    fill(Tiles.empty())
                    layer.tiles.forEach { (pos, tile) ->
                        setTileAt(pos, tile)
                    }
                }, 0)
    }

    private val Drawing.isNotLocked: Boolean
        get() = state.selectedLayer.isLocked.not()
}
