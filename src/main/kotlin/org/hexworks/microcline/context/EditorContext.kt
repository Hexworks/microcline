package org.hexworks.microcline.context

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawTools
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.drawtools.DrawTool
import org.hexworks.microcline.services.LayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer
import java.io.File

/**
 * Context object which contains the
 */
class EditorContext {

    /**
     * Acts as a drawing area.
     */
    val drawPanel: Panel = Components.panel()
            .withSize(Config.DRAW_SIZE)
            .withComponentRenderer(NoOpComponentRenderer())
            .build()

    /**
     * A [LayerEditor] implements the functionality for editing
     * draw layers (ordering, visibility, selection, etc).
     */
    val drawLayerEditor = LayerEditor.create(
            targetSurface = drawPanel,
            context = this)

    /**
     * Property for the currently selected [Tile] (glyph + colors).
     */
    val selectedTileProperty = createPropertyFrom<Tile>(Tiles
            .newBuilder()
            .withCharacter(DEFAULT_GLYPH)
            .withBackgroundColor(Palettes.XTERM_256.colors[0]) // ANSI Black
            .withForegroundColor(Palettes.XTERM_256.colors[7]) // ANSI White
            .buildCharacterTile())
    var selectedTile: Tile by selectedTileProperty.asDelegate()

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

    val currentLayers: List<DrawLayer>
        get() = drawLayerEditor.currentLayers

    companion object {
        private const val DEFAULT_GLYPH = Symbols.FACE_WHITE
    }
}
