package org.hexworks.microcline.components.layerfragments

import org.hexworks.microcline.data.events.LayerMovedDown
import org.hexworks.microcline.data.events.LayerMovedUp
import org.hexworks.microcline.layers.Layer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.util.CP437Utils


class MoveFragment(position: Position, upDisabled: Boolean, downDisabled: Boolean, layer: Layer) : Fragment {

    private val upButton = Components.button()
            .withPosition(Position.zero())
            .withText(CP437Utils.convertCp437toUnicode(24).toString()) // Up arrow
            .wrapSides(false)
            .build().apply {
                if (upDisabled) {
                    disable()
                }
                onComponentEvent(ACTIVATED) {
                    Zircon.eventBus.publish(LayerMovedUp(layer))
                    Processed
                }
            }

    private val downButton = Components.button()
            .withPosition(Position.create(1, 0))
            .withText(CP437Utils.convertCp437toUnicode(25).toString()) // Down arrow
            .wrapSides(false)
            .build().apply {
                if (downDisabled) {
                    disable()
                }
                onComponentEvent(ACTIVATED) {
                    Zircon.eventBus.publish(LayerMovedDown(layer))
                    Processed
                }
            }

    override val root = Components.panel()
            .withPosition(position)
            .withSize(2, 1)
            .build().apply {
                addComponent(upButton)
                addComponent(downButton)
            }

}
