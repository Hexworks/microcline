package org.hexworks.microcline.drawtools

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.LineFactory


class LineDrawTool : DrawTool {

    override val name = "Line"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        LineFactory.buildLine(command.startPosition, command.endPosition).forEach { pos ->
            surface.draw(command.tile, pos)
        }
    }

}
