package org.hexworks.microcline.views

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.ApplicationContext
import org.hexworks.microcline.fragments.DrawArea
import org.hexworks.microcline.fragments.ToolBelt
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.mvc.base.BaseView


class DrawView(context: ApplicationContext) : BaseView() {

    override val theme = Config.THEME

    private val content: VBox by lazy {
        Components.vbox()
                .withSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT)
                .build().apply {
                    addFragment(DrawArea(context.drawingComponent))
                    addFragment(ToolBelt(
                            drawing = context.selectedDrawing,
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
