package org.hexworks.microcline.state

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.DrawTools
import org.hexworks.microcline.data.DrawingLayer
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.data.events.DrawModeChanged
import org.hexworks.microcline.data.events.FileChanged
import org.hexworks.microcline.data.events.LayerOrderChanged
import org.hexworks.microcline.data.events.TileChanged
import org.hexworks.microcline.drawtools.DrawTool
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

    private val DEFAULT_GLYPH = CP437Utils.convertCp437toUnicode(1) // Smiley face
    private val EMPTY_BLOCK = Blocks.newBuilder<Tile>()
            .withEmptyTile(Tiles.empty())
            .withLayers(Tiles.empty())
            .build()

    /**
     * Manages [Layer] actions (create, remove, move, select, clear, etc...)
     */
    var layerRegistry = LayerRegistry()

    /**
     * Displays the visible [Layers] on the screen.
     * In the GameArea object we use 2 layers only:
     * - index 0: home to DrawController's overlays only where we highlight the selected tile
     *   and create the temporary shape layer;
     * - index 1: home to the actual layers of the picture.
     */
    val drawing = GameAreaBuilder<Tile, Block<Tile>>()
            .withActualSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, 2))
            .withVisibleSize(Size3D.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT, 2))
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
                    while (popOverlayAt(DrawingLayer.PICTURE.index).isPresent) {}

                    // The first layer is on the top, so pushing the overlays in reverse order.
                    layerRegistry.visibleLayers().reversed().forEach { layer ->
                        pushOverlayAt(layer.layer, DrawingLayer.PICTURE.index)
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
            .withBackgroundColor(Palettes.XTERM_256.colors[0]) // ANSI Black
            .withForegroundColor(Palettes.XTERM_256.colors[7]) // ANSI White
            .build()
        set(value) {
            field = value
            Zircon.eventBus.publish(TileChanged(value))
        }

    /**
     * Stores the currently selected [DrawTools]. When changed it sends a [DrawModeChanged] event.
     */
    var drawTool: DrawTool = DrawTools.FREEHAND.drawTool
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

    /**
     * Resets program state.
     */
    fun reset() {
        while(drawing.popOverlayAt(DrawingLayer.PICTURE.index).isPresent) {}
        layerRegistry = LayerRegistry().apply {
            create()
        }
        file = Maybe.empty()
    }

}
