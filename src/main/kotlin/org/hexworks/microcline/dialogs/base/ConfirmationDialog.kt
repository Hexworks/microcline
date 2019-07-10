package org.hexworks.microcline.dialogs.base

import org.hexworks.microcline.dialogs.result.No
import org.hexworks.microcline.dialogs.result.Yes
import org.hexworks.microcline.dialogs.result.YesNoResult
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType

/**
 * Creates a new [Modal] which can be used for a yes/no confirmation.
 * Closing it returns either a [Yes] or a [No].
 */
class ConfirmationDialog(screen: Screen,
                         title: String = "Are you sure?")
    : BaseDialog<YesNoResult>(
        screen = screen,
        contentSize = CONTENT_SIZE,
        title = title) {

    override val content = Components.panel().withSize(CONTENT_SIZE).build()

    init {
        buttonBar.addComponent(Components.button()
                .withText("Yes")
                .withAlignmentWithin(buttonBar, ComponentAlignment.BOTTOM_LEFT)
                .build().apply {
                    processComponentEvents(ComponentEventType.ACTIVATED) {
                        root.close(Yes)
                    }
                })

        buttonBar.addComponent(Components.button()
                .withText("No")
                .withAlignmentWithin(buttonBar, ComponentAlignment.BOTTOM_RIGHT)
                .build().apply {
                    processComponentEvents(ComponentEventType.ACTIVATED) {
                        root.close(No)
                    }
                })
    }

    companion object {

        private val CONTENT_SIZE = Sizes.create(20, 0)
    }
}
