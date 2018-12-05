package org.hexworks.microcline.panels

import org.hexworks.microcline.common.MouseButton
import org.hexworks.microcline.common.Palette
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer

const val PALETTE_P_SIZE_X = 16
const val PALETTE_P_SIZE_Y = 16

// TODO: This should be a Fragment instead
// TODO: A Fragment is a reusable object which has a `root` Component and some additional view logic for
// TODO: interacting with it.
// TODO: we shouldn't derive from `Panel` here.
class PalettePanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .withTitle("Palette")
                .withSize(Sizes.create(PALETTE_P_SIZE_X, PALETTE_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .withPosition(position)
                .withComponentRenderer(NoOpComponentRenderer())
                .build()
): Panel by panel {

    private var backgroundColor: TileColor = Palette[0]
    private var foregroundColor: TileColor = Palette[15]

    init {
        select(Positions.create(1, 1), MouseButton.RIGHT)
        select(Positions.create(15, 1), MouseButton.LEFT)
    }

    fun selectedBackgroundColor() = backgroundColor

    fun selectedForegroundColor() = foregroundColor

    fun getPanel() = panel

    fun select(position: Position, button: Int) {
        Palette.forEachIndexed { index, tileColor ->
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(tileColor)
                            .withCharacter(' ')
                            .build(),
                    Positions.create(index % PALETTE_P_SIZE_X, index / PALETTE_P_SIZE_Y).plus(Positions.offset1x1())
            )
        }
        val color = Palette[((position.y - 1) * PALETTE_P_SIZE_Y) + (position.x - 1)]
        when (button) {
            MouseButton.RIGHT -> {
                backgroundColor = color
            } else -> {
                foregroundColor = color
            }
        }
        if (backgroundColor == foregroundColor) {
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(color)
                            .withForegroundColor(color.invert())
                            .withCharacter('#')
                            .build(),
                    position
            )
        } else {
            val bgIndex = Palette.indexOf(backgroundColor)
            val fgIndex = Palette.indexOf(foregroundColor)
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(backgroundColor)
                            .withForegroundColor(backgroundColor.invert())
                            .withCharacter('B')
                            .build(),
                    Positions.create(bgIndex % PALETTE_P_SIZE_X, bgIndex / PALETTE_P_SIZE_Y).plus(Positions.offset1x1())
            )
            panel.draw(
                    Tiles.newBuilder()
                            .withBackgroundColor(foregroundColor)
                            .withForegroundColor(foregroundColor.invert())
                            .withCharacter('F')
                            .build(),
                    Positions.create(fgIndex % PALETTE_P_SIZE_X, fgIndex / PALETTE_P_SIZE_Y).plus(Positions.offset1x1())
            )
        }
    }
}
