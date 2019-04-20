package org.hexworks.microcline.components.dialogs

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.component.modal.ModalResult
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed


object YesModalResult : ModalResult

object NoModalResult : ModalResult

class YesButtonFragment(modal: Modal<ModalResult>, parent: Container) : Fragment {

    override val root = Components.button().withText("Yes")
            .withAlignmentWithin(parent, ComponentAlignment.BOTTOM_LEFT)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    modal.close(YesModalResult)
                    Processed
                }
            }
}

class NoButtonFragment(modal: Modal<ModalResult>, parent: Container) : Fragment {

    override val root = Components.button().withText("No")
            .withAlignmentWithin(parent, ComponentAlignment.BOTTOM_RIGHT)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    modal.close(NoModalResult)
                    Processed
                }
            }
}

class YesNoDialog(private val screen: Screen,
                  darkenPercent: Double = .5) : ModalFragment<ModalResult> {

    private val container = Components.panel()
            .withTitle("Are you sure?")
            .withSize(18, 3)
            .withBoxType(BoxType.DOUBLE)
            .build()

    override val root: Modal<ModalResult> by lazy {
        ModalBuilder.newBuilder<ModalResult>()
                .withComponent(container)
                .withParentSize(screen.size)
                .withDarkenPercent(darkenPercent)
                .withCenteredDialog(true)
                .build().also {
                    container.addFragment(YesButtonFragment(it, container))
                    container.addFragment(NoButtonFragment(it, container))
                    container.applyColorTheme(ColorThemes.amigaOs())
                }
    }

}
