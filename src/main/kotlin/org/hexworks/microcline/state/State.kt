package org.hexworks.microcline.state

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.Drawers
import org.hexworks.microcline.data.Palette
import org.hexworks.microcline.drawers.Drawer
import org.hexworks.microcline.data.events.DrawModeChanged
import org.hexworks.microcline.data.events.TileChanged
import org.hexworks.zircon.api.Blocks
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.util.CP437Utils
import java.util.*


object State {

    private val DEFAULT_GLYPH = CP437Utils.convertCp437toUnicode(1)
    private val EMPTY_BLOCK = Blocks.newBuilder<Tile>()
            .withEmptyTile(Tiles.empty())
            .withLayers(Tiles.empty())
            .build()

    val drawing = GameAreaBuilder<Tile, Block<Tile>>()
            .withActualSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, Config.MAX_LAYERS))
            .withVisibleSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, Config.MAX_LAYERS))
            .withLayersPerBlock(1)
            .withDefaultBlock(EMPTY_BLOCK)
            .build()

    val layers = LinkedList<Layer>().apply {
        push(Layers.newBuilder().withSize(drawing.visibleSize().to2DSize()).build())
    }.also {
        it.forEachIndexed { index, layer ->
            drawing.pushOverlayAt(layer, index)
        }
    }

    var selectedLayerIndex = 0

    /**
     * Stores the currently selected [Tile] (glyph + colors). When changed it sends a [TileChanged] event.
     */
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

    /**
     * Stores the currently selected [Drawers]. When changed it sends a [DrawModeChanged] event.
     */
    var drawer: Drawer = Drawers.FREEHAND.drawer
        set(value) {
            field = value
            Zircon.eventBus.publish(DrawModeChanged(value))
        }

}
