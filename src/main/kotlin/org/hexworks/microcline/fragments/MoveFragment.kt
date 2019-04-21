package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.services.DrawLayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed


class MoveFragment(position: Position,
                   layer: DrawLayer,
                   editor: DrawLayerEditor) : Fragment {

    private val upButton = Components.button()
            .withPosition(Position.zero())
            .withText("${Symbols.ARROW_UP}")
            .wrapSides(false)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    editor.moveLayerUp(layer)
                    Processed
                }
            }

    private val downButton = Components.button()
            .withPosition(Position.create(1, 0))
            .withText("${Symbols.ARROW_DOWN}")
            .wrapSides(false)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    editor.moveLayerDown(layer)
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
