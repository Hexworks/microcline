package org.hexworks.microcline.fragments

import org.hexworks.microcline.context.ApplicationContext
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED

/**
 * A [Tool] is an element for a toolbar which is responsible
 * for editing separate properties of an [ApplicationContext]
 */
class Tool(buttonLabel: String,
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
            .withDecorations(box())
            .withSize(Size.create(
                    width = button.width + label.width + visualization.width + 3,
                    height = 3))
            .build().apply {
                label.moveTo(Positions.topRightOf(button))
                visualization.moveTo(Positions.topRightOf(label).withRelativeX(1))
                addComponent(button)
                addComponent(label)
                addComponent(visualization)
                button.processComponentEvents(ACTIVATED) {
                    activationHandler()
                }
            }
}
