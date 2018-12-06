package org.hexworks.microcline.components

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer
import org.hexworks.zircon.internal.util.CP437Utils

const val GLYPH_P_SIZE_X = 16
const val GLYPH_P_SIZE_Y = 16

// TODO: This should be a Fragment instead
// TODO: A Fragment is a reusable object which has a `root` Component and some additional view logic for
// TODO: interacting with it.
// TODO: we shouldn't derive from `Panel` here.
class GlyphPanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .withTitle("Glyph")
                .withSize(Sizes.create(GLYPH_P_SIZE_X, GLYPH_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .withPosition(position)
                .withComponentRenderer(NoOpComponentRenderer())
                .build()
): Panel by panel {

    private var glyph: CharacterTile = Tiles.defaultTile()

    init {
        // TODO: this should be (1, 1) relative to the wrapper
        select(Positions.create(2, 2))
    }

    fun getPanel() = panel

    fun selectedGlyph() = glyph

    fun select(position: Position) {
        (0..255).forEach {
            this.draw(
                    Tiles.newBuilder()
                            .withForegroundColor(ANSITileColor.WHITE)
                            .withBackgroundColor(ANSITileColor.BLACK)
                            .withCharacter(CP437Utils.convertCp437toUnicode(it))
                            .build(),
                    Positions.create(it % GLYPH_P_SIZE_X, it / GLYPH_P_SIZE_Y).plus(Positions.offset1x1())
            )
        }
        glyph = this.getTileAt(position.minus(Positions.offset1x1())).get().asCharacterTile().get()
        this.setTileAt(
                position.minus(Positions.offset1x1()),
                glyph.withForegroundColor(ANSITileColor.RED).withBackgroundColor(ANSITileColor.CYAN)
        )
    }
}
