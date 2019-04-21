package org.hexworks.microcline.dialogs

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment.*
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

/**
 * Adds a Button with the text "OK" to the bottom of a [Modal] which
 * closes it when pressed.
 */
class OKButtonFragment(modal: Modal<EmptyModalResult>, parent: Container) : Fragment {

    override val root = Components.button().withText("OK")
            .withAlignmentWithin(parent, BOTTOM_CENTER)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    modal.close(EmptyModalResult)
                    Processed
                }
            }
}
