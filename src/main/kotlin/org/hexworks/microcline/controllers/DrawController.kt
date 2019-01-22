package org.hexworks.microcline.controllers

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.microcline.data.DrawCommand
import org.hexworks.microcline.data.events.MousePosition
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.internal.Zircon


class DrawController : MouseListener {

    var maybeTempLayer = Maybe.empty<Layer>()
    var maybeStartPosition = Maybe.empty<Position>()

    override fun mouseMoved(action: MouseAction) {
        val position = action.position - Position.offset1x1()
        Zircon.eventBus.publish(MousePosition(position.x, position.y))
    }

    override fun mousePressed(action: MouseAction) {
        val position = action.position - Position.offset1x1()
        maybeStartPosition = Maybe.of(position)

        if (!maybeTempLayer.isPresent) {
            maybeTempLayer = Maybe.of(
                    Layers.newBuilder()
                            .withSize(State.layers[State.selectedLayerIndex].size)
                            .build())
            State.drawing.pushOverlayAt(maybeTempLayer.get(), State.selectedLayerIndex)
        }

        State.drawer.draw(
                DrawCommand(State.tile, position, position, false),
                maybeTempLayer.get())
    }

    override fun mouseDragged(action: MouseAction) {
        maybeStartPosition.ifPresent { startPosition ->
            maybeTempLayer.ifPresent { tempLayer ->
                val position = action.position - Position.offset1x1()
                Zircon.eventBus.publish(MousePosition(position.x, position.y))

                tempLayer.clear()
                State.drawer.draw(
                        DrawCommand(State.tile, startPosition, position, false),
                        tempLayer
                )
            }
        }
    }

    override fun mouseReleased(action: MouseAction) {
        maybeStartPosition.ifPresent { startPosition ->
            maybeTempLayer.ifPresent { tempLayer ->
                val position = action.position - Position.offset1x1()

                State.drawer.draw(
                        DrawCommand(State.tile, startPosition, position, true),
                        State.layers[State.selectedLayerIndex])

                // Cleanup
                State.drawing.removeOverlay(tempLayer, State.selectedLayerIndex)
                maybeStartPosition = Maybe.empty()
                maybeTempLayer = Maybe.empty()
            }
        }
    }

}
