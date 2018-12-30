package org.hexworks.microcline.controllers

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.events.MousePosition
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.builder.graphics.LayerBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.LineFactory
import org.hexworks.zircon.internal.Zircon


class DrawAreaController(private val state: State,
                         private val panelPosition: Position) : MouseListener {

    private val initPosition = Position.create(1, 1)
    private var startPosition: Position = initPosition
    private lateinit var tempLayer: Layer

    override fun mousePressed(action: MouseAction) {
        // We always create a temporary layer for drawing.
        startPosition = action.position
        tempLayer = LayerBuilder.newBuilder()
                .withSize(state.layer.size)
                .build()
        state.tileGrid.pushLayer(tempLayer)
    }

    override fun mouseReleased(action: MouseAction) {
        if (state.mode == DrawMode.FREE) {
            if (startPosition == action.position) {
                drawTile(action, tempLayer)
            }
        }
        println("merge layer")
        tempLayer.drawOnto(state.tileGrid)
        state.tileGrid.removeLayer(tempLayer)
        startPosition = initPosition
    }

    override fun mouseMoved(action: MouseAction) {
        Zircon.eventBus.publish(MousePosition(action.position.x, action.position.y))
    }

    override fun mouseDragged(action: MouseAction) {
        Zircon.eventBus.publish(MousePosition(action.position.x, action.position.y))

        when (state.mode) {
            DrawMode.FREE -> {
                drawLine(action, tempLayer)
                startPosition = action.position
            }
            DrawMode.LINE -> {
                tempLayer.clear()
                drawLine(action, tempLayer)
            }
            DrawMode.ELLIPSE -> {
                tempLayer.clear()
                drawEllipse(action, tempLayer)
            }
            DrawMode.RECTANGLE -> {
                tempLayer.clear()
                drawRectangle(action, tempLayer)
            }
            else -> { }
        }
    }

    private fun drawTile(action: MouseAction, surface: DrawSurface) {
        surface.draw(state.tile, action.position.minus(panelPosition))
    }

    private fun drawLine(action: MouseAction, surface: DrawSurface) {
        val lineStart = startPosition.minus(startPosition)
        val lineEnd = action.position.minus(startPosition)
        val line = LineFactory.buildLine(lineStart, lineEnd)
        line.forEach {
            surface.draw(state.tile, startPosition.minus(panelPosition).plus(it))
        }
    }

    private fun drawEllipse(action: MouseAction, surface: DrawSurface) {
        val start = startPosition.minus(startPosition)
        val end = action.position.minus(startPosition)
        val ellipse = EllipseFactory.buildEllipse(start, end)
        ellipse.forEach {
            surface.draw(state.tile, startPosition.minus(panelPosition).plus(it))
        }
    }

    private fun drawRectangle(action: MouseAction, surface: DrawSurface) {
        val start = startPosition.minus(startPosition)
        val end = action.position.minus(startPosition)
        listOf(
                LineFactory.buildLine(start, Position.create(end.x, start.y)),
                LineFactory.buildLine(start, Position.create(start.x, end.y)),
                LineFactory.buildLine(end, Position.create(end.x, start.y)),
                LineFactory.buildLine(end, Position.create(start.x, end.y))
        ).map {
            it.forEach { pos ->
                surface.draw(state.tile, startPosition.minus(panelPosition).plus(pos))
            }
        }
    }
}
