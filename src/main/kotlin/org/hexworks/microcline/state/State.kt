package org.hexworks.microcline.state

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.common.Palette
import org.hexworks.microcline.config.NewConfig
import org.hexworks.microcline.events.DrawModeChanged
import org.hexworks.microcline.events.TileChanged
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.util.CP437Utils


class State(val tileGrid: TileGrid) {

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

    var layer: Layer = Layers.newBuilder()
            .withSize(Size.create(NewConfig.DRAW_AREA_WIDTH, NewConfig.DRAW_AREA_HEIGHT))
            .build()

    companion object {
        private val DEFAULT_GLYPH = CP437Utils.convertCp437toUnicode(1)
    }
}
