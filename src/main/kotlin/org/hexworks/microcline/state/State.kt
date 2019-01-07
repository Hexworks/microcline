package org.hexworks.microcline.state

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.common.Palette
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.controllers.DrawAreaController
import org.hexworks.microcline.drawings.Drawing
import org.hexworks.microcline.events.DrawModeChanged
import org.hexworks.microcline.events.TileChanged
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer
import org.hexworks.zircon.internal.util.CP437Utils
import java.lang.NumberFormatException
import java.util.*


class State(val tileGrid: TileGrid) {

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
     * Stores the currently selected [DrawMode]. When changed it sends a [DrawModeChanged] event.
     */
    var mode: DrawMode = DrawMode.FREE
        set(value) {
            field = value
            Zircon.eventBus.publish(DrawModeChanged(value))
        }

    lateinit var drawing: Drawing

    /**
     * Stores all draw layers and maps them to String IDs. This is a LinkedHashMap so that we can calculate
     * the ID of the next layer from the last created one.
     */
    val canvasMap = LinkedHashMap<String, Panel>()

    /**
     * Stores the String IDs of visible layers to be drawn in order.
     */
    val canvasList = LinkedList<String>()

    /**
     * Stores the currently selected draw layer.
     */
    var canvas: Panel = newCanvas()
//            .apply {
//        onMouseAction(DrawAreaController(this@State, Position.offset1x1()))
//    }

    /**
     * Creates a new Panel with a new ID and adds it to both canvasMap and canvasList.
     */
    fun newCanvas(): Panel {
        val panel = Components.panel()
                .withSize(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT)
                .withPosition(Position.zero())
                .withComponentRenderer(NoOpComponentRenderer())
                .build()

        // Generate the ID of the new Panel. This must be of format "Layer xxx" where xxx is a number
        // with leading zeros starting from 000.
        var num = 0
        if (!canvasMap.isEmpty()) {
            var lastID = ""
            while (canvasMap.iterator().hasNext()) {
                lastID = canvasMap.iterator().next().key
            }
            num = try { lastID.split("\\s+".toRegex())[-1].toInt() } catch (nfe: NumberFormatException) { 0 }
            num++
        }
        val id = "Layer " + num.toString().padStart(3, '0')

        canvasMap[id] = panel
        canvasList.push(id)

        return panel
    }

    fun clearCanvasControllers() {
        canvasMap.forEach { _, panel ->
            panel.onMouseAction(NOOP_CONTROLLER)
        }
    }

    companion object {
        private val DEFAULT_GLYPH = CP437Utils.convertCp437toUnicode(1)
        private val NOOP_CONTROLLER = object : MouseListener {}
    }
}
