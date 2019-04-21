package org.hexworks.microcline.data

import org.hexworks.microcline.drawtools.DrawTool
import org.hexworks.microcline.drawtools.EllipseDrawTool
import org.hexworks.microcline.drawtools.FreeHandDrawTool
import org.hexworks.microcline.drawtools.LineDrawTool
import org.hexworks.microcline.drawtools.RectangleDrawTool

/**
 * Contains an enumeration of all supported [DrawTool]s.
 */
enum class DrawTools(val drawTool: DrawTool) {
    FREEHAND(FreeHandDrawTool()),
    LINE(LineDrawTool()),
    RECTANGLE(RectangleDrawTool()),
    ELLIPSE(EllipseDrawTool()),
}
