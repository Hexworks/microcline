package org.hexworks.microcline.dialogs.base

import org.hexworks.microcline.dialogs.result.MaybeModalResult
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType

abstract class OkCancelDialog<T : Any>(
        screen: Screen,
        contentSize: Size,
        title: String) : BaseDialog<MaybeModalResult<T>>(screen, contentSize, title) {

    private val cancelButton = Components.button()
            .withText("Cancel")
            .withAlignmentWithin(buttonBar, ComponentAlignment.BOTTOM_RIGHT)
            .build().apply {
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    root.close(MaybeModalResult())
                }
            }

    val okButton = Components.button()
            .withText("OK")
            .withAlignmentAround(cancelButton, ComponentAlignment.LEFT_CENTER)
            .build()

    init {
        buttonBar.addComponent(okButton)
        buttonBar.addComponent(cancelButton)
    }
}
