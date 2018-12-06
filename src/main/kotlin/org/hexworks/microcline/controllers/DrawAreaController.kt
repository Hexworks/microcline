package org.hexworks.microcline.controllers

import org.hexworks.microcline.events.MousePosition
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.internal.Zircon


class DrawAreaController : MouseListener {

    override fun mouseMoved(action: MouseAction) {
        Zircon.eventBus.publish(MousePosition(action.position.x, action.position.y))
    }

    override fun mouseDragged(action: MouseAction) {
        Zircon.eventBus.publish(MousePosition(action.position.x, action.position.y))
    }

}
