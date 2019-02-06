package org.hexworks.microcline.data

import org.hexworks.microcline.drawers.*


enum class Drawers(val drawer: Drawer) {
    FREEHAND(FreeHandDrawer()),
    LINE(LineDrawer()),
    RECTANGLE(RectangleDrawer()),
    ELLIPSE(EllipseDrawer()),
}
