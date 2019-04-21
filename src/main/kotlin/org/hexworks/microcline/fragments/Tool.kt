package org.hexworks.microcline.fragments

import org.hexworks.microcline.context.EditorContext
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed

/**
 * A [Tool] is an element for a [ToolBelt] which is responsible
 * for editing separate properties of an [EditorContext]
 */
class Tool(position: Position,
           buttonLabel: String,
           visualization: Component,
           activationHandler: () -> Unit = {}) : Fragment {

    private val button = Components.button()
            .withPosition(Position.zero())
            .withText(buttonLabel)
            .build()

    private val label = Components.label()
            .withText(":")
            .build()

    override val root = Components.panel()
            .withPosition(position)
            .wrapWithBox(true)
            .withSize(Size.create(
                    width = button.width + label.width + visualization.width + 3,
                    height = 3))
            .build().apply {
                label.moveTo(Positions.topRightOf(button))
                visualization.moveTo(Positions.topRightOf(label).withRelativeX(1))
                addComponent(button)
                addComponent(label)
                addComponent(visualization)
                button.onComponentEvent(ACTIVATED) {
                    activationHandler()
                    Processed
                }
            }
}
