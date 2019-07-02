package org.hexworks.microcline.services

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.events.api.Subscription
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.DrawCommand
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.DrawSurfaces
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.transformTileAt
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.graphics.TileGraphics

class LayerEditor(private val targetSurface: DrawSurface,
                  private val context: EditorContext,
                  private val layers: MutableList<DrawLayer>) {

    private val subscriptions = mutableMapOf<Identifier, List<Subscription>>()

    val selectedLayerProperty: Property<DrawLayer> = createPropertyFrom(layers.first())

    val currentLayers: List<DrawLayer>
        get() = layers.toList()

    private val size: Size
        get() = targetSurface.size

    private val base = DrawSurfaces.tileGraphicsBuilder()
            .withSize(size)
            .build()
            .fill(Tiles.defaultTile())

    private val selectedLayer: DrawLayer by selectedLayerProperty.asDelegate()
    private var startPosition = Maybe.empty<Position>()
    private var highlightPosition = Maybe.empty<Position>()
    private var currentEdit = Maybe.empty<TileGraphics>()

    init {
        updateOnChanges(selectedLayer)
        redrawLayers()
    }

    /**
     * Creates a highlight at the given [position].
     */
    fun highlight(position: Position) {
        if (size.containsPosition(position)) {
            highlightPosition = Maybe.of(position)
        }
        redrawLayers()
    }

    /**
     * Adds a new [DrawLayer] at the top.
     */
    fun addNewLayer(): DrawLayer {
        val drawLayer = DrawLayer(
                size = size,
                initialLabel = "Layer ${layers.size}")
        layers.add(drawLayer)
        updateOnChanges(drawLayer)
        return drawLayer
    }

    fun removeLayer(drawLayer: DrawLayer) {
        layers.remove(drawLayer)
        subscriptions[drawLayer.id]?.forEach {
            it.cancel()
        }
        redrawLayers()
    }

    fun startDrawingAt(position: Position) {
        if (selectedLayer.isLocked.not()) {
            startPosition = Maybe.of(position)
            currentEdit = Maybe.of(DrawSurfaces.tileGraphicsBuilder()
                    .withSize(size)
                    .build().apply {
                        draw(selectedLayer.content)
                    })
            refreshDrawingAt(position)
            redrawLayers()
        }
    }

    fun refreshDrawingAt(position: Position, finished: Boolean = false) {
        if (selectedLayer.isLocked.not()) {
            startPosition.map { startPosition ->
                highlight(position)
                val graphics = DrawSurfaces.tileGraphicsBuilder()
                        .withSize(size)
                        .build()
                graphics.draw(selectedLayer.content)
                context.currentTool.draw(
                        DrawCommand(
                                tile = context.selectedTile,
                                startPosition = startPosition,
                                endPosition = position,
                                finished = finished),
                        graphics)
                currentEdit = Maybe.of(graphics)
            }
            redrawLayers()
        }
    }

    fun stopDrawingAt(position: Position) {
        if (selectedLayer.isLocked.not()) {
            refreshDrawingAt(position, true)
            currentEdit.map {
                selectedLayer.clear()
                selectedLayer.draw(it)
            }
            startPosition = Maybe.empty()
            currentEdit = Maybe.empty()
            redrawLayers()
        }
    }

    fun moveLayerUp(layer: DrawLayer) {
        val idx = layers.indexOf(layer)
        if (canMoveLayerUp(idx)) {
            layers.removeAt(idx)
            layers.add(idx - 1, layer)
        }
        redrawLayers()
    }


    fun moveLayerDown(layer: DrawLayer) {
        val idx = layers.indexOf(layer)
        if (canMoveLayerDown(idx)) {
            layers.removeAt(idx)
            layers.add(idx + 1, layer)
        }
        redrawLayers()
    }

    fun redrawLayers() {
        val image = base.createCopy()
        layers.filter { it.isVisible }.forEach { layer ->
            if (layer.isSelected && currentEdit.isPresent) {
                image.draw(currentEdit.get())
            } else {
                image.draw(layer.content)
            }
        }
        highlightPosition.map { pos ->
            image.transformTileAt(pos) { it.withAddedModifiers(Modifiers.border()) }
        }
        targetSurface.draw(image)
    }

    private fun updateOnChanges(drawLayer: DrawLayer) {
        val newSubs = mutableListOf<Subscription>()
        newSubs.add(drawLayer.selectedProperty.onChange {
            if (it.newValue) {
                selectedLayerProperty.value = drawLayer
                layers.minus(drawLayer).forEach { layer ->
                    layer.isSelected = false
                }
            }
        })
        newSubs.add(drawLayer.visibleProperty.onChange {
            redrawLayers()
        })
        subscriptions[drawLayer.id] = newSubs
    }

    private fun canMoveLayerUp(idx: Int) = idx > 0

    private fun canMoveLayerDown(idx: Int) = idx >= 0 && idx < layers.size - 1

    companion object {

        fun create(targetSurface: DrawSurface,
                   context: EditorContext) = LayerEditor(
                targetSurface = targetSurface,
                context = context,
                layers = mutableListOf(DrawLayer(
                        size = targetSurface.size,
                        initialLabel = "Layer 0",
                        initialSelected = true)))
    }
}
