package org.hexworks.microcline.view

import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.input.Input
import org.hexworks.zircon.internal.util.CP437Utils

class DrawView(private val tileGrid: TileGrid) : View {

    private val screen = Screens.createScreenFor(tileGrid)

    init {
        // ----- Glyphs
        val glyphPanel = Components.panel()
                .wrapWithBox()
                .title("Glyph")
                .size(Sizes.create(18, 18))
                .position(Positions.offset1x1())
                .build()

        (0..255).forEach {
            glyphPanel.draw(
                    Tiles.newBuilder()
                            .foregroundColor(ANSITileColor.WHITE)
                            .character(CP437Utils.convertCp437toUnicode(it))
                            .build(),
                    Positions.create(it % 16, it / 16).plus(Positions.offset1x1())
            )
        }

        // ----- Palette
        val palettePanel = Components.panel()
                .wrapWithBox()
                .title("Palette")
                .size(Sizes.create(18, 18))
                .position(Positions.defaultPosition().relativeToBottomOf(glyphPanel))
                .build()

        Palette.forEachIndexed { index, tileColor ->
            palettePanel.draw(
                    Tiles.newBuilder()
                            .backgroundColor(tileColor)
                            .character(' ')
                            .build(),
                    Positions.create(index % 16, index / 16).plus(Positions.offset1x1())
            )
        }

        // ----- Tools
        val toolsPanel = Components.panel()
                .wrapWithBox()
                .title("Tools")
                .size(Sizes.create(18, 9))
                .position(Positions.defaultPosition().relativeToBottomOf(palettePanel))
                .build()

        val toolOptions = Components.radioButtonGroup()
                .size(Sizes.create(16, 7))
                .position(Positions.defaultPosition())
                .build()
        toolOptions.addOption("select", "Select")
        toolOptions.addOption("cell", "Cell")
        toolOptions.addOption("line", "Line")
        toolOptions.addOption("rectangle", "Rectangle")
        toolOptions.addOption("oval", "Oval")
        toolOptions.addOption("fill", "Fill")
        toolOptions.addOption("text", "Text")
        toolsPanel.addComponent(toolOptions)

        // ----- Layers
        val layersPanel = Components.panel()
                .wrapWithBox()
                .title("Layers")
                .size(Sizes.create(18, 15))
                .position(Positions.defaultPosition().relativeToBottomOf(toolsPanel))
                .build()

        // ----- Draw surface
        val fileName = "<Untitled>"
        val drawPanel = Components.panel()
                .wrapWithBox()
                .title("Draw surface: $fileName")
                .size(Sizes.create(tileGrid.size().width().minus(20), tileGrid.size().height().minus(2)))
                .position(Positions.defaultPosition().relativeToRightOf(glyphPanel))
                .build()


        screen.addComponent(glyphPanel)
        screen.addComponent(palettePanel)
        screen.addComponent(toolsPanel)
        screen.addComponent(layersPanel)
        screen.addComponent(drawPanel)
    }

    override fun display() {
        screen.display()
    }

    override fun respondToUserInput(input: Input): View {
//        return when (input.getInputType()) {
//            Enter -> WinView(tileGrid)
//            Escape -> LoseView(tileGrid)
//            else -> this
//        }
        return this
    }
}
