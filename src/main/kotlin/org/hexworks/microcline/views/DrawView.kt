package org.hexworks.microcline.views

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.fragments.DrawArea
import org.hexworks.microcline.fragments.ToolBelt
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.mvc.base.BaseView


class DrawView(context: EditorContext) : BaseView() {

    override val theme = Config.THEME

    private val content: VBox by lazy {
        Components.vbox()
                .withSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT)
                .build().apply {
                    addFragment(DrawArea(
                            context = context))
                    addFragment(ToolBelt(
                            screen = screen,
                            context = context))
                }
    }

    override fun onDock() {
        screen.addComponent(content)
    }

    override fun onUndock() {
        screen.removeComponent(content)
    }

}
