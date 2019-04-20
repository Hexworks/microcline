package org.hexworks.microcline.components

import org.hexworks.microcline.extensions.onMouseEvent
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.uievent.MouseEvent
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_PRESSED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase.TARGET
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class Tool(position: Position,
           labelText: String,
           component: Component,
           clickHandler: (MouseEvent) -> Unit,
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
        label.onMouseEvent(MOUSE_PRESSED, TARGET) { action ->
            clickHandler(action)
            Processed
        }
        component.moveTo(Position.topRightOf(label))
        wrapper.addComponent(component)
        component.onMouseEvent(MOUSE_PRESSED, TARGET) { action ->
            clickHandler(action)
            Processed
        }
    }

}
