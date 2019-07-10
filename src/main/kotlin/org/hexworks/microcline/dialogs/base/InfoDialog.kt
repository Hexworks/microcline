package org.hexworks.microcline.dialogs.base

import org.hexworks.microcline.dialogs.result.MaybeModalResult
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType

abstract class InfoDialog<T : Any>(
        screen: Screen,
        contentSize: Size,
        title: String) : BaseDialog<MaybeModalResult<T>>(screen, contentSize, title) {

    private val closeButton = Components.button()
            .withText("Close")
            .withAlignmentWithin(buttonBar, ComponentAlignment.CENTER)
            .build().apply {
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    root.close(MaybeModalResult())
                }
            }

    init {
        buttonBar.addComponent(closeButton)
    }

}
