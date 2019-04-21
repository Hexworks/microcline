package org.hexworks.microcline.services

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.DrawCommand
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.DrawSurfaces
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.graphics.TileGraphics

class DrawLayerEditor(private val size: Size,
                      private val layers: MutableList<DrawLayer>,
                      private val gameArea: GameArea<CharacterTile, Block<CharacterTile>>,
                      private val context: EditorContext) {

    init {
        addNewLayer()
    }

    val selectedLayerProperty: Property<DrawLayer> = createPropertyFrom(layers.first())
    var originalGraphics = Maybe.empty<TileGraphics>()
    var previousHighlight = Maybe.empty<Position>()

    private val selectedLayer: DrawLayer by selectedLayerProperty.asDelegate()
    private var startPosition = Maybe.empty<Position>()

    /**
     * Creates a highlight at the given [position] using the given [tile].
     */
    fun highlight(position: Position) {
        removeCurrentHighlight()
        // TODO:
    }

    /**
     * Adds a new [DrawLayer] at the top.
     */
    fun addNewLayer(): DrawLayer {
        val drawLayer = DrawLayer(
                size = size,
                initialLabel = "Layer 0",
                initialSelected = true)
        layers.add(drawLayer)
        drawLayer.selectedProperty.onChange {
            if (it.newValue) {
                layers.minus(drawLayer).forEach { layer ->
                    layer.isSelected = false
                }
            }
        }
        gameArea.pushOverlayAt(
                layer = drawLayer.layer,
                level = 1)
        return drawLayer
    }

    fun removeLayer(drawLayer: DrawLayer) {
        if (drawLayer.isSelected.not()) {
            layers.remove(drawLayer)
            gameArea.removeOverlay(drawLayer.layer, 1)
        }
    }

    fun startDrawingAt(position: Position) {
        startPosition = Maybe.of(position)
        originalGraphics = Maybe.of(selectedLayer.layer.toTileGraphics())
        refreshDrawingAt(position)
    }

    fun refreshDrawingAt(position: Position, finished: Boolean = false) {
        startPosition.map { startPosition ->
            val newGraphics = DrawSurfaces.tileGraphicsBuilder()
                    .withSize(Config.DRAW_SIZE)
                    .build()
            originalGraphics.map {
                newGraphics.draw(it)
            }
            context.currentTool.draw(
                    DrawCommand(
                            tile = context.selectedTile,
                            startPosition = startPosition,
                            endPosition = position,
                            finished = finished),
                    newGraphics)
            selectedLayer.clear()
            selectedLayer.draw(newGraphics)
        }
    }

    fun stopDrawingAt(position: Position) {
        refreshDrawingAt(position, true)
        startPosition = Maybe.empty()
        originalGraphics = Maybe.empty()
    }

    fun moveLayerUp(layer: DrawLayer) {
        TODO("not implemented")
    }

    fun moveLayerDown(layer: DrawLayer) {
        TODO("not implemented")
    }

    private fun removeCurrentHighlight() {
        previousHighlight.map {
            selectedLayer.layer.setTileAt(
                    position = it,
                    tile = selectedLayer.layer
                            .getTileAt(it).get()
                            .withRemovedModifiers(Modifiers.border()))
        }
    }

    companion object {

        fun create(size: Size,
                   gameArea: GameArea<CharacterTile, Block<CharacterTile>>,
                   context: EditorContext) = DrawLayerEditor(
                size = size,
                layers = mutableListOf(),
                gameArea = gameArea,
                context = context)
    }
}
