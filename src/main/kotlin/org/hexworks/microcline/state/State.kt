package org.hexworks.microcline.state

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.common.Palette
import org.hexworks.microcline.events.DrawModeChanged
import org.hexworks.microcline.events.TileChanged
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.util.CP437Utils


class State {

    var tile: Tile = Tiles
            .newBuilder()
            .withCharacter(DEFAULT_GLYPH)
            .withBackgroundColor(Palette[0]) // ANSI Black
            .withForegroundColor(Palette[7]) // ANSI White
            .build()
        set(value) {
            field = value
            Zircon.eventBus.publish(TileChanged(value))
        }

    var mode: DrawMode = DrawMode.FREE
        set(value) {
            field = value
            Zircon.eventBus.publish(DrawModeChanged(value))
        }

    companion object {
        private val DEFAULT_GLYPH = CP437Utils.convertCp437toUnicode(1)
    }
}
