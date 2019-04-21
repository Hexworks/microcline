package org.hexworks.microcline.drawtools

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.LineFactory

class RectangleDrawTool : DrawTool {

    override val name = "Rectangle"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        listOf(
                LineFactory.buildLine(
                        command.startPosition,
                        Positions.create(command.endPosition.x, command.startPosition.y)),
                LineFactory.buildLine(
                        command.startPosition,
                        Positions.create(command.startPosition.x, command.endPosition.y)),
                LineFactory.buildLine(
                        command.endPosition,
                        Positions.create(command.endPosition.x, command.startPosition.y)),
                LineFactory.buildLine(
                        command.endPosition,
                        Positions.create(command.startPosition.x, command.endPosition.y)))
                .flatMap {
                    it.positions()
                }.forEach { pos ->
                    surface.draw(command.tile, pos)
                }

    }

}
