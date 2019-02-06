package org.hexworks.microcline.drawers

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.LineFactory


class RectangleDrawer : Drawer {

    override fun name(): String = "Rectangle"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        listOf(
                LineFactory.buildLine(command.startPosition, Position.create(command.endPosition.x, command.startPosition.y)),
                LineFactory.buildLine(command.startPosition, Position.create(command.startPosition.x, command.endPosition.y)),
                LineFactory.buildLine(command.endPosition, Position.create(command.endPosition.x, command.startPosition.y)),
                LineFactory.buildLine(command.endPosition, Position.create(command.startPosition.x, command.endPosition.y))
        ).map {
            it.forEach { pos ->
                surface.draw(command.tile, pos)
            }
        }

    }

}
