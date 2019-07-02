package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.datatypes.extensions.flatMap
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.MouseButton
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.extensions.handleMouseEvents
import org.hexworks.microcline.extensions.processMouseEvents
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.box
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
            .withSize(40, 23)
            .withDecorations(box(title = "Pick Tile and Color", boxType = BoxType.DOUBLE))
            .build().apply {
                addComponent(Components.hbox()
                        .withSpacing(1)
                        .withSize(38, 20)
                        .build().apply {
                            addComponent(Components.panel()
                                    .withDecorations(box(title = "Tile"))
                                    .withPosition(Position.offset1x1())
                                    .withSize(18, 18)
                                    .withComponentRenderer(NoOpComponentRenderer())
                                    .build().apply {

                                        // Select current tile.
                                        selectGlyph(this, glyphPosition())

                                        handleMouseEvents(MOUSE_PRESSED, TARGET) { event ->
                                            // Calculate relative position of the mouse click.
                                            val relativePosition = event.position
                                                    .minus(absolutePosition)

                                            // Do not select border glyphs.
                                            if (relativePosition.x < 1 || relativePosition.x > 16 || relativePosition.y < 1 || relativePosition.y > 16) {
                                                return@handleMouseEvents Pass
                                            }

                                            selectGlyph(this, relativePosition)
                                            Processed
                                        }
                                    })

                            addComponent(Components.panel()
                                    .withDecorations(box(title = "Color"))
                                    .withPosition(Position.offset1x1())
                                    .withSize(18, 18)
                                    .build().apply {
                                        addComponent(Components.panel()
                                                .withSize(16, 16)
                                                .withComponentRenderer(NoOpComponentRenderer())
                                                .build().apply {
                                                    val palettePicker = this
                                                    // Select current colors.
                                                    selectColor(
                                                            container = palettePicker,
                                                            position = context.selectedTile.foregroundColor.colorPosition,
                                                            button = MouseButton.LEFT.id)
                                                    selectColor(
                                                            container = palettePicker,
                                                            position = context.selectedTile.backgroundColor.colorPosition,
                                                            button = MouseButton.RIGHT.id)

                                                    processMouseEvents(MOUSE_PRESSED, TARGET) { action ->
                                                        if (action.button != MouseButton.CENTER.id) {
                                                            selectColor(
                                                                    container = palettePicker,
                                                                    position = action.position
                                                                            .minus(palettePicker.absolutePosition),
                                                                    button = action.button)
                                                        }
                                                    }
                                                })
                                    })

                        })
            }

    private fun selectColor(container: Container, position: Position, button: Int) {
        // Redraw palette.
        Palettes.XTERM_256.colors.forEachIndexed { index, tileColor ->
            container.draw(
                    drawable = Tiles.newBuilder()
                            .withBackgroundColor(tileColor)
                            .withCharacter(' ')
                            .build(),
                    position = Positions.create(index % 16, index / 16))
        }

        val color = Palettes.XTERM_256.colors[(position.y * 16) + position.x]
        val (fg, bg) = when (button) {
            MouseButton.LEFT.id -> {
                color to context.selectedTile.backgroundColor
            }
            else -> {
                context.selectedTile.foregroundColor to color
            }
        }

        // Mark the selected colors.
        if (fg == bg) {
            container.draw(Tiles.newBuilder()
                    .withBackgroundColor(color)
                    .withForegroundColor(color.invert())
                    .withCharacter('#')
                    .build(),
                    position
            )
        } else {
            container.draw(Tiles.newBuilder()
                    .withBackgroundColor(bg)
                    .withForegroundColor(bg.invert())
                    .withCharacter('B')
                    .build(),
                    bg.colorPosition
            )
            container.draw(Tiles.newBuilder()
                    .withBackgroundColor(fg)
                    .withForegroundColor(fg.invert())
                    .withCharacter('F')
                    .build(),
                    fg.colorPosition
            )
        }

        context.selectedTile = context.selectedTile.withBackgroundColor(bg).withForegroundColor(fg)
    }

    private fun glyphPosition(): Position {
        val idx = CP437Utils.fetchCP437IndexForChar(context.selectedTile.asCharacterTile().get().character)
        return Positions.create(idx % 16, idx / 16).plus(Position.offset1x1())
    }

    private val TileColor.colorPosition: Position
        get() {
            val idx = Palettes.XTERM_256.colors.indexOf(this)
            return Positions.create(idx % 16, idx / 16)
        }

    private fun selectGlyph(panel: Panel, position: Position) {
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

        val selectedTile = panel.getTileAt(position)
                .flatMap { it.asCharacterTile() }
                .get()
        panel.setTileAt(position, selectedTile.withBackgroundColor(ANSITileColor.CYAN).withForegroundColor(ANSITileColor.RED))

        context.selectedTile = context.selectedTile.asCharacterTile().get().withCharacter(selectedTile.character)
    }

}
