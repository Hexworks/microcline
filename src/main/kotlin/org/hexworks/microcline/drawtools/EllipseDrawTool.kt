package org.hexworks.microcline.drawtools

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.EllipseFactory


class EllipseDrawTool : DrawTool {

    override val name = "Ellipse"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        EllipseFactory.buildEllipse(command.startPosition, command.endPosition).forEach { pos ->
            surface.draw(command.tile, pos)
        }
    }

}
