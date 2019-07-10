package org.hexworks.microcline.data

import org.hexworks.microcline.commands.Command
import org.hexworks.microcline.commands.EllipseDraw
import org.hexworks.microcline.commands.Erase
import org.hexworks.microcline.commands.FreeHandDraw
import org.hexworks.microcline.commands.LineDraw
import org.hexworks.microcline.commands.RectangleDraw
import org.hexworks.zircon.api.data.Position

/**
 * Contains an enumeration of all supported draw modes.
 */
enum class DrawMode(val label: String,
                    val commandFn: (touchedPositions: List<Position>) -> Command) {
    FREE_HAND("Free Hand", {
        FreeHandDraw(it.toSet())
    }),
    LINE("Line", {
        LineDraw(it.first(), it.last())
    }),
    RECTANGLE("Rectangle", {
        RectangleDraw(it.first(), it.last())
    }),
    ELLIPSE("Ellipse", {
        EllipseDraw(it.first(), it.last())
    }),
    ERASE("Erase", {
        Erase(it.toSet())
    });

    /**
     * Creates a [Command] representing this [DrawMode] for the given
     * [touchedPositions].
     */
    fun asCommand(touchedPositions: List<Position>): Command {
        return commandFn(touchedPositions)
    }

}
