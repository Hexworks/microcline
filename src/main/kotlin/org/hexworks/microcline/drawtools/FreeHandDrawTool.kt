package org.hexworks.microcline.drawtools

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.LineFactory


class FreeHandDrawTool : DrawTool {

    private var maybePreviousEnd = Maybe.empty<Position>()
    private val positions = mutableSetOf<Position>()

    override val name = "Free hand"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        val startPosition = if (maybePreviousEnd.isPresent) maybePreviousEnd.get() else command.startPosition

        LineFactory.buildLine(startPosition, command.endPosition).forEach { pos ->
            positions.add(pos)
        }
        positions.forEach {
            surface.draw(command.tile, it)
        }

        if (command.finished) {
            maybePreviousEnd = Maybe.empty()
            positions.clear()
            return
        }

        maybePreviousEnd = Maybe.of(command.endPosition)
    }

}
