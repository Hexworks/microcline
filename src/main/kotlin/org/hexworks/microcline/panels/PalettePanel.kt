package org.hexworks.microcline.panels

import org.hexworks.microcline.common.Palette
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position

const val PALETTE_P_SIZE_X = 16
const val PALETTE_P_SIZE_Y = 16

class PalettePanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox()
                .title("Palette")
                .size(Sizes.create(PALETTE_P_SIZE_X, PALETTE_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .position(position)
                .build()
): Panel by panel {

    private var backgroundColor: TileColor = TileColors.defaultBackgroundColor()
    private var foregroundColor: TileColor = TileColors.defaultForegroundColor()

    init {
        select(Positions.create(2, 2))
    }

    fun getBackgroundColor() = backgroundColor

    fun getForegroundColor() = foregroundColor

    fun getPanel() = panel

    private fun select(position: Position) {
        Palette.forEachIndexed { index, tileColor ->
            this.draw(
                    Tiles.newBuilder()
                            .backgroundColor(tileColor)
                            .character(' ')
                            .build(),
                    Positions.create(index % PALETTE_P_SIZE_X, index / PALETTE_P_SIZE_Y).plus(Positions.offset1x1())
            )
        }
    }
}
