package org.hexworks.microcline.view

import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile

class DrawConfig {
    private var foregroundColor: TileColor = TileColor.defaultForegroundColor()
    private var backgroundColor: TileColor = TileColor.defaultBackgroundColor()
    private var glyph: Tile = Tile.defaultTile()

    fun setForegroundColor(color: TileColor) {
        foregroundColor = color
    }

    fun setBackgroundColor(color: TileColor) {
        backgroundColor = color
    }

    fun setGlyph(glyph: Tile) {
        this.glyph = glyph
    }

    fun getForegroundColor() = foregroundColor

    fun getBackgroundColor() = backgroundColor

    fun getGlyph() = glyph
}
