package org.hexworks.microcline.drawers

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.shape.LineFactory


class FreeHandDrawer : Drawer {

    private var maybePreviousEnd = Maybe.empty<Position>()
    private val positions = mutableSetOf<Position>()

    override fun name(): String = "Free hand"

    override fun draw(command: DrawCommand, surface: DrawSurface) {
        val startPosition = if (maybePreviousEnd.isPresent) maybePreviousEnd.get() else command.startPosition

        LineFactory.buildLine(startPosition, command.endPosition).forEach {
            positions.add(it)
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
