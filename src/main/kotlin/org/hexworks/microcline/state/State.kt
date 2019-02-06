package org.hexworks.microcline.state

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.Drawers
import org.hexworks.microcline.data.Palette
import org.hexworks.microcline.data.events.DrawModeChanged
import org.hexworks.microcline.data.events.FileChanged
import org.hexworks.microcline.data.events.LayerOrderChanged
import org.hexworks.microcline.data.events.TileChanged
import org.hexworks.microcline.drawers.Drawer
import org.hexworks.microcline.layers.LayerRegistry
import org.hexworks.zircon.api.Blocks
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.util.CP437Utils
import java.io.File


object State {

    const val FILE_NONAME = "<noname>"
    private val DEFAULT_GLYPH = CP437Utils.convertCp437toUnicode(1) // Smiley face
    private val EMPTY_BLOCK = Blocks.newBuilder<Tile>()
            .withEmptyTile(Tiles.empty())
            .withLayers(Tiles.empty())
            .build()

    /**
     * Manages [Layer] actions (create, remove, move, select, clear, etc...)
     */
    val layerRegistry = LayerRegistry()

    /**
     * Displays the visible [Layers] on the screen.
     */
    val drawing = GameAreaBuilder<Tile, Block<Tile>>()
            .withActualSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, Config.MAX_LAYERS))
            .withVisibleSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, Config.MAX_LAYERS))
            .withLayersPerBlock(1)
            .withDefaultBlock(EMPTY_BLOCK)
            .build().apply {
                Zircon.eventBus.subscribe<LayerOrderChanged> {
                    // In case of a LayerOrderChanged event, we remove all overlays and re-add the visible ones.
                    // Layer order can be changed by the following:
                    // - new layer created
                    // - layer removed
                    // - layer position changed (moved up/down)
                    // - layer visibility changed
                    do {
                        val x = popOverlayAt(1)
                    } while (x.isPresent)

                    // The first layer is on the top, so pushing the overlays in reverse order.
                    layerRegistry.visibleLayers().reversed().forEach { layer ->
                        pushOverlayAt(layer.layer, 1)
                    }
                }

                // We're now subscribed to the event, so create the first layer.
                layerRegistry.create()
            }

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

    /**
     * Stores the currently selected file.
     */
    var file = Maybe.empty<File>()
        set(value) {
            field = value
            Zircon.eventBus.publish(FileChanged(if (!value.isEmpty()) value.get().name else Config.NONAME_FILE))
        }

}
