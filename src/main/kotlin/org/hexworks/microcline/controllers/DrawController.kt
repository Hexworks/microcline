package org.hexworks.microcline.controllers

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.drawings.Drawing
import org.hexworks.microcline.drawings.FreeHandDrawing
import org.hexworks.microcline.events.MousePosition
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.builder.graphics.LayerBuilder
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.api.shape.EllipseFactory
import org.hexworks.zircon.api.shape.LineFactory
import org.hexworks.zircon.internal.Zircon


class DrawController(private val state: State,
                     private val panel: Panel) : MouseListener {

    var drawing: Drawing? = null

    override fun mousePressed(action: MouseAction) {
        println("DrawController mousePressed")
        drawing = when (state.mode) {
            DrawMode.FREE -> {
                FreeHandDrawing(state, action.position)
            }
            else -> {
                null
            }
        }

        if (drawing != null) {
            panel.also { panel.addComponent(it) }
        }
    }

//    private val initPosition = Position.create(1, 1)
//    private var startPosition: Position = initPosition
//    private lateinit var tempLayer: Layer

//    override fun mouseReleased(action: MouseAction) {
//        if (state.mode == DrawMode.FREE) {
//            if (startPosition == action.position) {
//                drawTile(action, tempLayer)
//            }
//        }
//        println("merge layer")
//        tempLayer.drawOnto(state.canvas)
//        state.tileGrid.removeLayer(tempLayer)
//        startPosition = initPosition
//    }
//
//    override fun mousePressed(action: MouseAction) {
//        // We always create a temporary layer for drawing.
//        startPosition = action.position
//        tempLayer = LayerBuilder.newBuilder()
//                .withSize(state.canvas.size)
//                .withOffset(Position.offset1x1())
//                .build()
//        state.tileGrid.pushLayer(tempLayer)
//    }

    override fun mouseMoved(action: MouseAction) {
        panel.clear() // TODO: doesn't work???
//        panel.draw(Tile.empty().withModifiers(Modifiers.border()), action.position.minus(panel.position))
        panel.draw(state.tile, action.position.minus(panel.position))

        Zircon.eventBus.publish(MousePosition(action.position.x, action.position.y))
    }

//    override fun mouseDragged(action: MouseAction) {
//        Zircon.eventBus.publish(MousePosition(action.position.x, action.position.y))
//
//        when (state.mode) {
//            DrawMode.FREE -> {
//                drawLine(action, tempLayer)
//                startPosition = action.position
//            }
//            DrawMode.LINE -> {
//                tempLayer.clear()
//                drawLine(action, tempLayer)
//            }
//            DrawMode.ELLIPSE -> {
//                tempLayer.clear()
//                drawEllipse(action, tempLayer)
//            }
//            DrawMode.RECTANGLE -> {
//                tempLayer.clear()
//                drawRectangle(action, tempLayer)
//            }
//            else -> { }
//        }
//    }
//
//    private fun drawTile(action: MouseAction, surface: DrawSurface) {
//        surface.draw(state.tile, action.position.minus(panelPosition))
//    }
//
//    private fun drawLine(action: MouseAction, surface: DrawSurface) {
//        val lineStart = startPosition.minus(startPosition)
//        val lineEnd = action.position.minus(startPosition)
//        val line = LineFactory.buildLine(lineStart, lineEnd)
//        line.forEach {
//            surface.draw(state.tile, startPosition.minus(panelPosition).plus(it))
//        }
//    }
//
//    private fun drawEllipse(action: MouseAction, surface: DrawSurface) {
//        val start = startPosition.minus(startPosition)
//        val end = action.position.minus(startPosition)
//        val ellipse = EllipseFactory.buildEllipse(start, end)
//        ellipse.forEach {
//            surface.draw(state.tile, startPosition.minus(panelPosition).plus(it))
//        }
//    }
//
//    private fun drawRectangle(action: MouseAction, surface: DrawSurface) {
//        val start = startPosition.minus(startPosition)
//        val end = action.position.minus(startPosition)
//        listOf(
//                LineFactory.buildLine(start, Position.create(end.x, start.y)),
//                LineFactory.buildLine(start, Position.create(start.x, end.y)),
//                LineFactory.buildLine(end, Position.create(end.x, start.y)),
//                LineFactory.buildLine(end, Position.create(start.x, end.y))
//        ).map {
//            it.forEach { pos ->
//                surface.draw(state.tile, startPosition.minus(panelPosition).plus(pos))
//            }
//        }
//    }
}
