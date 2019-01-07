package org.hexworks.microcline.drawings

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.LineFactory


abstract class DrawingBase(override val startPosition: Position,
                           private val state: State) : Drawing {

    override val panel = Components.panel()
            .withSize(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT)
            .withPosition(1, 1)
            .build().apply {
                applyColorTheme(ColorThemes.empty())
                onMouseAction(controller) // TODO: controller is null here...
            }

    protected fun drawTile(position: Position) {
        panel.draw(state.tile, position.minus(panel.position))
    }

    protected fun drawLine(position: Position) {
        val lineStart = startPosition.minus(startPosition)
        val lineEnd = position.minus(startPosition)
        val line = LineFactory.buildLine(lineStart, lineEnd)
        line.forEach {
            panel.draw(state.tile, startPosition.minus(panel.position).plus(it))
        }
    }

    protected fun drawEllipse(position: Position) {
        val start = startPosition.minus(startPosition)
        val end = position.minus(startPosition)
        val ellipse = EllipseFactory.buildEllipse(start, end)
        ellipse.forEach {
            panel.draw(state.tile, startPosition.minus(panel.position).plus(it))
        }
    }

    protected fun drawRectangle(position: Position) {
        val start = startPosition.minus(startPosition)
        val end = position.minus(startPosition)
        listOf(
                LineFactory.buildLine(start, Position.create(end.x, start.y)),
                LineFactory.buildLine(start, Position.create(start.x, end.y)),
                LineFactory.buildLine(end, Position.create(end.x, start.y)),
                LineFactory.buildLine(end, Position.create(start.x, end.y))
        ).map {
            it.forEach { pos ->
                panel.draw(state.tile, startPosition.minus(panel.position).plus(pos))
            }
        }
    }

}
