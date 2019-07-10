package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.flatMap
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.MouseButton
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.dialogs.base.OkCancelDialog
import org.hexworks.microcline.dialogs.result.MaybeModalResult
import org.hexworks.microcline.extensions.handleMouseEvents
import org.hexworks.microcline.extensions.processMouseEvents
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_PRESSED
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase.TARGET
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer
import org.hexworks.zircon.internal.util.CP437Utils


class TileSelectorDialog(screen: Screen,
                         private var selectedTile: Tile) : OkCancelDialog<Tile>(screen, CONTENT_SIZE, "Pick Tile and Color") {

    override val content = Components.hbox()
            .withSpacing(1)
            .withSize(CONTENT_SIZE)
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
                                                position = selectedTile.foregroundColor.colorPosition,
                                                button = MouseButton.LEFT.id)
                                        selectColor(
                                                container = palettePicker,
                                                position = selectedTile.backgroundColor.colorPosition,
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

                applyColorTheme(Config.THEME)
            }

    init {
        okButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            root.close(MaybeModalResult(Maybe.of(selectedTile)))
        }
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
                color to selectedTile.backgroundColor
            }
            else -> {
                selectedTile.foregroundColor to color
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

        selectedTile = selectedTile.withBackgroundColor(bg).withForegroundColor(fg)
    }

    private fun glyphPosition(): Position {
        val idx = CP437Utils.fetchCP437IndexForChar(selectedTile.asCharacterTile().get().character)
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

        val tile = panel.getTileAt(position)
                .flatMap { it.asCharacterTile() }
                .get()
        panel.setTileAt(position, tile.withBackgroundColor(ANSITileColor.CYAN).withForegroundColor(ANSITileColor.RED))

        selectedTile = selectedTile.asCharacterTile().get().withCharacter(tile.character)
    }

    companion object {

        private val CONTENT_SIZE = Sizes.create(38, 20)
    }
}
