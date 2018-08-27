package org.hexworks.microcline.panels

import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.util.Consumer
import org.hexworks.zircon.internal.util.CP437Utils

const val SIZE_X = 16
const val SIZE_Y = 16

class GlyphPanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox()
                .title("Glyph")
                .size(Sizes.create(SIZE_X, SIZE_Y).plus(Sizes.create(2, 2)))
                .position(position)
                .build()
): Panel by panel {

    private var glyph: CharacterTile = Tiles.defaultTile()

    init {
        this.onMouseReleased(object : Consumer<MouseAction> {
            override fun accept(p: MouseAction) {
                // Only the left mouse button can be used
                if (p.button != MouseButton.LEFT) {
                    return
                }
                // Ignore if clicked on the border
                // TODO: must be a better way... possible zircon feature?
                if ((p.position.x <= position.x) || (p.position.x > position.x + SIZE_X) ||
                        (p.position.y <= position.y) || (p.position.y > position.y + SIZE_Y)) {
                    return
                }
                select(p.position)
            }
        })

        // TODO: this should be (1, 1) relative to the panel
        select(Positions.create(2, 2))
    }

    fun getPanel() = panel

    fun getGlyph() = glyph

    private fun select(position: Position) {
        (0..255).forEach {
            this.draw(
                    Tiles.newBuilder()
                            .foregroundColor(ANSITileColor.WHITE)
                            .backgroundColor(ANSITileColor.BLACK)
                            .character(CP437Utils.convertCp437toUnicode(it))
                            .build(),
                    Positions.create(it % SIZE_X, it / SIZE_Y).plus(Positions.offset1x1())
            )
        }
        glyph = this.getRelativeTileAt(position.minus(Positions.offset1x1())).get().asCharacterTile().get()
        this.setRelativeTileAt(
                position.minus(Positions.offset1x1()),
                glyph.withForegroundColor(ANSITileColor.RED).withBackgroundColor(ANSITileColor.CYAN)
        )
    }
}
