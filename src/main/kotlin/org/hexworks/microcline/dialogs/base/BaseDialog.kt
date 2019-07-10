package org.hexworks.microcline.dialogs.base

import org.hexworks.microcline.config.Config
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.component.modal.ModalResult
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.screen.Screen

abstract class BaseDialog<T : ModalResult>(screen: Screen,
                                           contentSize: Size,
                                           title: String = "",
                                           darkenPercent: Double = .5) : ModalFragment<T> {

    abstract val content: Component

    val buttonBar = Components.panel()
            .withSize(contentSize.width, 1)
            .build()

    private val container = Components.panel()
            .withSize(contentSize)
            .build()

    private val wrapper: Component = Components.vbox()
            .withSize(contentSize + Sizes.create(2, 3))
            .withDecorations(if (title.isBlank()) box() else box(title = title))
            .build().apply {
                addComponent(container)
                addComponent(buttonBar)
            }

    final override val root: Modal<T> by lazy {
        container.addComponent(content)
        ModalBuilder.newBuilder<T>()
                .withComponent(wrapper)
                .withParentSize(screen.size)
                .withDarkenPercent(darkenPercent)
                .withCenteredDialog(true)
                .build().apply {
                    applyColorTheme(Config.THEME)
                }
    }

}
