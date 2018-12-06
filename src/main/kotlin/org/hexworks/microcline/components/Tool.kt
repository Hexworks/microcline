package org.hexworks.microcline.components

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class Tool(position: Position,
           labelText: String,
           component: Component,
           clickHandler: (MouseAction) -> Unit,

           label: Label = Components.label()
                   .withPosition(Position.zero())
                   .withText("$labelText: ")
                   .build()
) {

    val wrapper: Panel = Components.panel()
            .withPosition(position)
            .withSize(Size.create(label.size.width + component.size.width + 1, 1))
            .withComponentRenderer(NoOpComponentRenderer())
            .build()

    init {
        wrapper.addComponent(label)
        label.onMouseAction(object : MouseListener {
            override fun mousePressed(action: MouseAction) {
                clickHandler(action)
            }
        })
        component.moveTo(Position.topRightOf(label))
        wrapper.addComponent(component)
        component.onMouseAction(object : MouseListener {
            override fun mousePressed(action: MouseAction) {
                clickHandler(action)
            }
        })
    }

}
