package org.hexworks.microcline.components.dialogs

import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.MouseButton
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.extensions.onMouseEvent
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_PRESSED
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase.TARGET
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer
import org.hexworks.zircon.internal.util.CP437Utils


class TileSelectorDialog(screen: Screen,
                         private val context: EditorContext) : BaseDialog(screen) {

    override val content = Components.panel()
            .withTitle("Tile")
            .withSize(40, 23)
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

                            glyphPanel.onMouseEvent(MOUSE_PRESSED, TARGET) { action ->
                                // Only the left mouse button can be used.
                                if (action.button != MouseButton.LEFT.id) {
                                    return@onMouseEvent Pass
                                }

                                // Calculate relative position of the mouse click.
                                val relativePosition = action.position
                                        .minus(glyphPanel.position)
                                        .minus(panel.position)

                                // Do not select border glyphs.
                                if (relativePosition.x < 1 || relativePosition.x > 16 || relativePosition.y < 1 || relativePosition.y > 16) {
                                    return@onMouseEvent Pass
                                }

                                selectGlyph(glyphPanel, relativePosition)
                                Processed
                            }
                        }

                val palettePanel = Components.panel()
                        .wrapWithBox()
                        .withPosition(Position.topRightOf(glyphPanel))
                        .withSize(18, 18)
                        .withComponentRenderer(NoOpComponentRenderer())
                        .build().also { palettePanel ->
                            // Select current colors.
                            selectColor(palettePanel, colorPosition(context.tile.foregroundColor), MouseButton.LEFT.id)
                            selectColor(palettePanel, colorPosition(context.tile.backgroundColor), MouseButton.RIGHT.id)

                            palettePanel.onMouseEvent(MOUSE_PRESSED, TARGET) { action ->
                                // Center mouse button is ignored.
                                if (action.button == MouseButton.CENTER.id) {
                                    return@onMouseEvent Pass
                                }

                                // Calculate relative position of the mouse click.
                                val relativePosition = action.position
                                        .minus(palettePanel.position)
                                        .minus(panel.position)

                                // Do not select border glyphs.
                                if (relativePosition.x < 1 || relativePosition.x > 16 || relativePosition.y < 1 || relativePosition.y > 16) {
                                    return@onMouseEvent Pass
                                }

                                selectColor(palettePanel, relativePosition, action.button)
                                Processed
                            }

                        }

                panel.addComponent(glyphPanel)
                panel.addComponent(palettePanel)
            }

    private fun selectColor(panel: Panel, position: Position, button: Int) {
        // Redraw palette.
        Palettes.XTERM_256.colors.forEachIndexed { index, tileColor ->
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(tileColor)
                            .withCharacter(' ')
                            .build(),
                    Positions.create(index % 16, index / 16).plus(Positions.offset1x1())
            )
        }

        // Find color and assign it to foreground or background based on the mouse button.
        val color = Palettes.XTERM_256.colors[((position.y - 1) * 16) + (position.x - 1)]
        val bg: TileColor
        val fg: TileColor
        when (button) {
            MouseButton.LEFT.id -> {
                fg = color
                bg = context.tile.backgroundColor
            }
            else -> {
                fg = context.tile.foregroundColor
                bg = color
            }
        }

        // Mark the selected colors.
        if (fg == bg) {
            panel.draw(Tiles.newBuilder()
                    .withBackgroundColor(color)
                    .withForegroundColor(color.invert())
                    .withCharacter('#')
                    .build(),
                    position
            )
        } else {
            panel.draw(Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(bg.invert())
                    .withCharacter('B')
                    .build(),
                    colorPosition(bg)
            )
            panel.draw(Tiles.newBuilder()
                    .withBackgroundColor(fg)
                    .withForegroundColor(fg.invert())
                    .withCharacter('F')
                    .build(),
                    colorPosition(fg)
            )
        }

        // Update context.
        context.tile = context.tile.withBackgroundColor(bg).withForegroundColor(fg)
    }

    private fun colorPosition(color: TileColor): Position {
        val idx = Palettes.XTERM_256.colors.indexOf(color)
        return Positions.create(idx % 16, idx / 16).plus(Position.offset1x1())
    }

    private fun glyphPosition(): Position {
        val idx = CP437Utils.fetchCP437IndexForChar(context.tile.asCharacterTile().get().character)
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

        // Update context.
        val fg = context.tile.foregroundColor
        val bg = context.tile.backgroundColor
        context.tile = tile.withBackgroundColor(bg).withForegroundColor(fg)
    }

}
