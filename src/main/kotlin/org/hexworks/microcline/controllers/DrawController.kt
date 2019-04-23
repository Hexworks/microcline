package org.hexworks.microcline.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.extensions.onAnyMouseEvent
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.uievent.MouseEvent
import org.hexworks.zircon.api.uievent.MouseEventHandler
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_DRAGGED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_MOVED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_PRESSED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_RELEASED
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase
import org.hexworks.zircon.api.uievent.UIEventResponse


class DrawController(context: EditorContext) : MouseEventHandler {

    private val logger = LoggerFactory.getLogger(DrawController::class)

    private val drawLayerEditor = context.drawLayerEditor

    init {
        context.drawPanel.onAnyMouseEvent(this)
    }

    override fun handle(event: MouseEvent, phase: UIEventPhase): UIEventResponse {
        return when (event.type) {
            MOUSE_MOVED -> mouseMoved(event)
            MOUSE_PRESSED -> mousePressed(event)
            MOUSE_DRAGGED -> mouseDragged(event)
            MOUSE_RELEASED -> mouseReleased(event)
            else -> {
                logger.debug("Mouse event not handled: $event")
                Pass
            }
        }
    }

    private fun mouseMoved(action: MouseEvent): UIEventResponse {
        val position = action.position - Position.offset1x1()

        drawLayerEditor.highlight(
                position = position)

        return Processed
    }

    private fun mousePressed(action: MouseEvent): UIEventResponse {
        val position = action.position - Position.offset1x1()

        drawLayerEditor.startDrawingAt(position)

        return Processed
    }

    private fun mouseDragged(action: MouseEvent): UIEventResponse {
        val position = action.position - Position.offset1x1()

        drawLayerEditor.refreshDrawingAt(position)

        return Processed
    }

    private fun mouseReleased(action: MouseEvent): UIEventResponse {
        val position = action.position - Position.offset1x1()

        drawLayerEditor.stopDrawingAt(position)

        return Processed
    }

}
