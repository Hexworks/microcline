package org.hexworks.microcline.drawings

import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener


class FreeHandDrawing(state: State, startPosition: Position) : DrawingBase(startPosition, state) {


    override val controller = object : MouseListener {
        override fun mouseDragged(action: MouseAction) {
            drawLine(action.position)
        }

        override fun mouseReleased(action: MouseAction) {
            drawTile(action.position)
        }
    }

}
