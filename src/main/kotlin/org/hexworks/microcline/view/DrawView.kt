package org.hexworks.microcline.view

import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.input.Input
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.util.Consumer
import org.hexworks.zircon.internal.util.CP437Utils

class DrawView(tileGrid: TileGrid) : View {

    private val screen = Screens.createScreenFor(tileGrid)
    private var selectedGlyph: CharacterTile = Tiles.defaultTile()

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

        glyphPanel.onMouseReleased(object : Consumer<MouseAction> {
            override fun accept(t: MouseAction) {
                glyphPanel.getTileAt(t.position).map { tile ->
                    tile.asCharacterTile().map { ct ->
                        println("position: ${t.position}, tile: $ct")
                        selectedGlyph = selectedGlyph.withCharacter(ct.character)
                    }
                }
            }
        })

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
        drawPanel.onMousePressed(object : Consumer<MouseAction> {
            override fun accept(t: MouseAction) {
                drawPanel.draw(
                        drawable = selectedGlyph,
                        position = t.position.minus(drawPanel.position()))
            }
        })

        listOf(glyphPanel, palettePanel, toolsPanel, layersPanel, drawPanel).forEach {
            screen.addComponent(it)
        }
        screen.applyColorTheme(THEME)
        drawPanel.getEffectiveSize().fetchPositions().forEach {
            drawPanel.setRelativeTileAt(it, Tiles.defaultTile())
        }
    }

    override fun display() {
        screen.display()
    }

    override fun respondToUserInput(input: Input): View {
        return this
    }

    companion object {
        val THEME = ColorThemes.monokaiBlue()
    }
}
