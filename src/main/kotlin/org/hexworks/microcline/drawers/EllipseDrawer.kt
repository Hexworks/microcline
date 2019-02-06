package org.hexworks.microcline.drawers

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.EllipseFactory


class EllipseDrawer : Drawer {

    override fun name(): String = "Ellipse"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        EllipseFactory.buildEllipse(command.startPosition, command.endPosition).forEach {
            surface.draw(command.tile, it)
        }
    }

}
