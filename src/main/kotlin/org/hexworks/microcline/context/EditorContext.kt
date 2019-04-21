package org.hexworks.microcline.context

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawTools
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.drawtools.DrawTool
import org.hexworks.microcline.services.DrawLayerEditor
import org.hexworks.zircon.api.Blocks
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.graphics.Symbols
import java.io.File

/**
 * Context object which contains the
 */
class EditorContext {

    /**
     * Displays the visible [Layer] on the screen.
     */
    val gameArea = GameAreaBuilder<CharacterTile, Block<CharacterTile>>()
            .withActualSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, 2))
            .withVisibleSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, 2))
            .withLayersPerBlock(1)
            .withDefaultBlock(EMPTY_BLOCK)
            .build()

    /**
     * A [DrawLayerEditor] implements the functionality for editing
     * draw layers (ordering, visibility, selection, etc).
     */
    val drawLayerEditor = DrawLayerEditor.create(
            size = Config.DRAW_SIZE,
            gameArea = gameArea,
            context = this)

    /**
     * Property for the currently selected [Tile] (glyph + colors).
     */
    val selectedTileProperty = createPropertyFrom(Tiles
            .newBuilder()
            .withCharacter(DEFAULT_GLYPH)
            .withBackgroundColor(Palettes.XTERM_256.colors[0]) // ANSI Black
            .withForegroundColor(Palettes.XTERM_256.colors[7]) // ANSI White
            .buildCharacterTile())
    var selectedTile: CharacterTile by selectedTileProperty.asDelegate()

    /**
     * Property for the currently selected [DrawTool].
     */
    val currentToolProperty = createPropertyFrom(DrawTools.FREEHAND.drawTool)
    var currentTool: DrawTool by currentToolProperty.asDelegate()


    /**
     * Property for the currently selected file.
     */
    val currentFileProperty = createPropertyFrom(Maybe.empty<File>())
    var currentFile: Maybe<File> by currentFileProperty.asDelegate()

    val selectedLayerProperty = drawLayerEditor.selectedLayerProperty
    val selectedLayer: DrawLayer by selectedLayerProperty.asDelegate()

    companion object {
        private const val DEFAULT_GLYPH = Symbols.FACE_WHITE
        private val EMPTY_BLOCK = Blocks.newBuilder<CharacterTile>()
                .withEmptyTile(Tiles.empty())
                .withLayers(Tiles.empty())
                .build()
    }
}
