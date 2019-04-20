package org.hexworks.microcline.components.dialogs

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.component.modal.EmptyModalResult

abstract class Dialog(private val screen: Screen,
                      withClose: Boolean = true,
                      darkenPercent: Double = .5) : ModalFragment<EmptyModalResult> {

    abstract val container: Container

    final override val root: Modal<EmptyModalResult> by lazy {
        ModalBuilder.newBuilder<EmptyModalResult>()
                .withComponent(container)
                .withParentSize(screen.size)
                .withDarkenPercent(darkenPercent)
                .withCenteredDialog(true)
                .build().also {
                    if (withClose) {
                        container.addFragment(OKButtonFragment(it, container))
                    }
                    container.applyColorTheme(ColorThemes.amigaOs())
                }
    }

}
