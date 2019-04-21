package org.hexworks.microcline.dialogs

import org.hexworks.microcline.config.Config
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment.BOTTOM_LEFT
import org.hexworks.zircon.api.component.ComponentAlignment.BOTTOM_RIGHT
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.component.modal.ModalResult
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed

/**
 * Creates a new [Modal] which can be used for a yes/no confirmation.
 * Closing it returns either a [Yes] or a [No].
 */
class ConfirmationDialog(private val screen: Screen,
                         darkenPercent: Double = .5,
                         message: String = "Are you sure?",
                         size: Size = Sizes.create(18, 3))
    : ModalFragment<ModalResult> {

    private val container = Components.panel()
            .withTitle(message)
            .withSize(size)
            .withBoxType(BoxType.DOUBLE)
            .build()

    override val root: Modal<ModalResult> by lazy {
        ModalBuilder.newBuilder<ModalResult>()
                .withComponent(container)
                .withParentSize(screen.size)
                .withDarkenPercent(darkenPercent)
                .withCenteredDialog(true)
                .build().also { modal ->
                    container.addComponent(Components.button()
                            .withText("Yes")
                            .withAlignmentWithin(container, BOTTOM_LEFT)
                            .build().apply {
                                onComponentEvent(ACTIVATED) {
                                    modal.close(Yes)
                                    Processed
                                }
                            })
                    container.addComponent(Components.button()
                            .withText("No")
                            .withAlignmentWithin(container, BOTTOM_RIGHT)
                            .build().apply {
                                onComponentEvent(ACTIVATED) {
                                    modal.close(No)
                                    Processed
                                }
                            })
                    container.applyColorTheme(Config.THEME)
                }
    }

    object Yes : ModalResult

    object No : ModalResult
}
