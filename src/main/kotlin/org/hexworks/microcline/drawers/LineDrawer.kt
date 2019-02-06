package org.hexworks.microcline.drawers

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.LineFactory


class LineDrawer : Drawer {

    override fun name(): String = "Line"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        LineFactory.buildLine(command.startPosition, command.endPosition).forEach {
            surface.draw(command.tile, it)
        }
    }

}
