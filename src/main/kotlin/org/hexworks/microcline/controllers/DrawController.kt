package org.hexworks.microcline.controllers

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.microcline.data.DrawCommand
import org.hexworks.microcline.data.events.MousePosition
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Layers
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.internal.Zircon


class DrawController : MouseListener {

    // All layers have the same size so it's OK to grab the first layer's.
    private val layerSize = State.layers[0].size

    private var maybeTempLayer = Maybe.empty<Layer>()
    private var maybeStartPosition = Maybe.empty<Position>()
    private var selectionLayer = Layers.newBuilder()
            .withSize(layerSize)
            .build().also {
                State.drawing.pushOverlayAt(it, 0)
            }

    override fun mouseMoved(action: MouseAction) {
        val position = action.position - Position.offset1x1()

        // Select tile on mouse position.
        selectionLayer.clear()
        selectionLayer.draw(
                State.tile.withModifiers(Modifiers.border()),
                position)

        Zircon.eventBus.publish(MousePosition(position.x, position.y))
    }

    override fun mousePressed(action: MouseAction) {
        val position = action.position - Position.offset1x1()
        maybeStartPosition = Maybe.of(position)

        // Create the temporary layer.
        if (!maybeTempLayer.isPresent) {
            maybeTempLayer = Maybe.of(
                    Layers.newBuilder()
                            .withSize(layerSize)
                            .build())
            State.drawing.pushOverlayAt(maybeTempLayer.get(), State.selectedLayerIndex)
        }

        // Draw the initial tile (if drawer draws is at all) with border.
        State.drawer.draw(
                DrawCommand(State.tile.withModifiers(Modifiers.border()), position, position, false),
                maybeTempLayer.get())
    }

    override fun mouseDragged(action: MouseAction) {
        maybeStartPosition.ifPresent { startPosition ->
            maybeTempLayer.ifPresent { tempLayer ->
                val position = action.position - Position.offset1x1()
                Zircon.eventBus.publish(MousePosition(position.x, position.y))

                // Clear selected tile.
                selectionLayer.clear()

                // Draw the temporary thing.
                tempLayer.clear()
                State.drawer.draw(
                        DrawCommand(State.tile, startPosition, position, false),
                        tempLayer)

                // Draw border around the tile on mouse position.
                // This can be empty (depends on the drawer), so get the tile on position first.
                tempLayer.draw(
                        tempLayer.getTileAt(position).get().withModifiers(Modifiers.border()),
                        position)
            }
        }
    }

    override fun mouseReleased(action: MouseAction) {
        maybeStartPosition.ifPresent { startPosition ->
            maybeTempLayer.ifPresent { tempLayer ->
                val position = action.position - Position.offset1x1()

                // Select tile on mouse position.
                selectionLayer.clear()
                selectionLayer.draw(
                        State.tile.withModifiers(Modifiers.border()),
                        position)

                // Draw the thing onto the real layer.
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

    override fun mouseExited(action: MouseAction) {
        selectionLayer.clear()
    }

}
