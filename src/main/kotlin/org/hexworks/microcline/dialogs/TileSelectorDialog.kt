package org.hexworks.microcline.dialogs

import org.hexworks.microcline.common.MouseButton
import org.hexworks.microcline.common.Palette
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer
import org.hexworks.zircon.internal.util.CP437Utils


class TileSelectorDialog(screen: Screen, private val state: State) : Dialog(screen) {

    override val container = Components.panel()
            .withTitle("Tile")
            .withSize(40,25)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().also { panel ->
                val glyphPanel = Components.panel()
                        .wrapWithBox()
                        .withPosition(Position.offset1x1())
                        .withSize(18, 18)
                        .withComponentRenderer(NoOpComponentRenderer())
                        .build().also { glyphPanel ->
                            // Select current tile.
                            selectGlyph(glyphPanel, glyphPosition())

                            glyphPanel.onMouseAction(object : MouseListener {
                                override fun mousePressed(action: MouseAction) {
                                    // Only the left mouse button can be used.
                                    if (action.button != MouseButton.LEFT.id) {
                                        return
                                    }

                                    // Calculate relative position of the mouse click.
                                    val relativePosition = action.position
                                            .minus(glyphPanel.position)
                                            .minus(panel.position)

                                    // Do not select border glyphs.
                                    if (relativePosition.x < 1 || relativePosition.x > 16 || relativePosition.y < 1 || relativePosition.y > 16) {
                                        return
                                    }

                                    selectGlyph(glyphPanel, relativePosition)
                                }
                            })
                        }

                val palettePanel = Components.panel()
                        .wrapWithBox()
                        .withPosition(Position.topRightOf(glyphPanel))
                        .withSize(18, 18)
                        .withComponentRenderer(NoOpComponentRenderer())
                        .build().also { palettePanel ->
                            // Select current colors.
                            selectColor(palettePanel, colorPosition(state.tile.foregroundColor), MouseButton.LEFT.id)
                            selectColor(palettePanel, colorPosition(state.tile.backgroundColor), MouseButton.RIGHT.id)

                            palettePanel.onMouseAction(object : MouseListener {
                                override fun mousePressed(action: MouseAction) {
                                    // Center mouse button is ignored.
                                    if (action.button == MouseButton.CENTER.id) {
                                        return
                                    }

                                    // Calculate relative position of the mouse click.
                                    val relativePosition = action.position
                                            .minus(palettePanel.position)
                                            .minus(panel.position)

                                    // Do not select border glyphs.
                                    if (relativePosition.x < 1 || relativePosition.x > 16 || relativePosition.y < 1 || relativePosition.y > 16) {
                                        return
                                    }

                                    selectColor(palettePanel, relativePosition, action.button)
                                }
                            })
                        }

                panel.addComponent(glyphPanel)
                panel.addComponent(palettePanel)
            }

    private fun selectColor(panel: Panel, position: Position, button: Int) {
        // Redraw palette.
        Palette.forEachIndexed { index, tileColor ->
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(tileColor)
                            .withCharacter(' ')
                            .build(),
                    Positions.create(index % 16, index / 16).plus(Positions.offset1x1())
            )
        }

        // Find color and assign it to foreground or background based on the mouse button.
        val color = Palette[((position.y - 1) * 16) + (position.x - 1)]
        val bg: TileColor
        val fg: TileColor
        when (button) {
            MouseButton.LEFT.id -> {
                fg = color
                bg = state.tile.backgroundColor
            } else -> {
                fg = state.tile.foregroundColor
                bg = color
            }
        }

        // Mark the selected colors.
        if (fg == bg) {
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(color)
                            .withForegroundColor(color.invert())
                            .withCharacter('#')
                            .build(),
                    position
            )
        } else {
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(bg)
                            .withForegroundColor(bg.invert())
                            .withCharacter('B')
                            .build(),
                    colorPosition(bg)
            )
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(fg)
                            .withForegroundColor(fg.invert())
                            .withCharacter('F')
                            .build(),
                    colorPosition(fg)
            )
        }

        // Update State.
        state.tile = state.tile.withBackgroundColor(bg).withForegroundColor(fg)
    }

    private fun colorPosition(color: TileColor): Position {
        val idx = Palette.indexOf(color)
        return Positions.create(idx % 16, idx / 16).plus(Position.offset1x1())
    }

    private fun glyphPosition(): Position {
        val idx = CP437Utils.fetchCP437IndexForChar(state.tile.asCharacterTile().get().character)
        return Positions.create(idx % 16, idx / 16).plus(Position.offset1x1())
    }

    private fun selectGlyph(panel: Panel, position: Position) {
        // Redraw glyph table.
        (0..255).forEach {
            panel.draw(
                    Tiles.newBuilder()
                            .withForegroundColor(ANSITileColor.WHITE)
                            .withBackgroundColor(ANSITileColor.BLACK)
                            .withCharacter(CP437Utils.convertCp437toUnicode(it))
                            .build(),
                    Positions.create(it % 16, it / 16).plus(Positions.offset1x1())
            )
        }

        // Paint selected glyph.
        val tile = panel.getTileAt(position).get()
        panel.setTileAt(position, tile.withBackgroundColor(ANSITileColor.CYAN).withForegroundColor(ANSITileColor.RED))

        // Update State.
        val fg = state.tile.foregroundColor
        val bg = state.tile.backgroundColor
        state.tile = tile.withBackgroundColor(bg).withForegroundColor(fg)
    }

}
