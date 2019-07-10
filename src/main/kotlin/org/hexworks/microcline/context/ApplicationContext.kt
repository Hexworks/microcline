package org.hexworks.microcline.context

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawMode
import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.extensions.DrawComponent
import org.hexworks.zircon.api.Blocks
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import java.io.File

/**
 * Context object which contains the shared state between different
 * components of Microcline.
 */
class ApplicationContext(
        initialTile: Tile,
        val drawLayersArea: GameArea<CharacterTile, Block<CharacterTile>> =
                GameAreaBuilder.newBuilder<CharacterTile, Block<CharacterTile>>()
                        .withActualSize(GAME_AREA_SIZE)
                        .withVisibleSize(GAME_AREA_SIZE)
                        .withDefaultBlock(Blocks.newBuilder<CharacterTile>()
                                .withEmptyTile(Tiles.empty())
                                .build())
                        .withLayersPerBlock(1)
                        .build(),
        val drawingComponent: DrawComponent = Components.gameComponent<CharacterTile, Block<CharacterTile>>()
                .withSize(GAME_AREA_SIZE.to2DSize())
                .withGameArea(drawLayersArea)
                .withVisibleSize(GAME_AREA_SIZE)
                .build(),
        initialDrawing: Drawing = Drawing.create(tile = initialTile),
        initialDrawMode: DrawMode = DrawMode.FREE_HAND) {

    val selectedTileProperty = createPropertyFrom(initialTile)
    var selectedTile: Tile by selectedTileProperty.asDelegate()

    val selectedDrawingProperty = createPropertyFrom(initialDrawing)
    var selectedDrawing: Drawing by selectedDrawingProperty.asDelegate()

    val selectedDrawModeProperty = createPropertyFrom(initialDrawMode)
    var selectedDrawMode: DrawMode by selectedDrawModeProperty.asDelegate()

    val selectedLayerProperty = createPropertyFrom(selectedDrawing.state.selectedLayer)
    var selectedLayer: DrawLayer by selectedLayerProperty.asDelegate()

    // TODO: implement
    val selectedFileProperty = createPropertyFrom(Maybe.empty<File>())
    var selectedFile: Maybe<File> by selectedFileProperty.asDelegate()

    companion object {
        private val GAME_AREA_SIZE = Sizes.create3DSize(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, 1)
    }
}
