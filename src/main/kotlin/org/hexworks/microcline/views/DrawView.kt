package org.hexworks.microcline.views

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.fragments.DrawArea
import org.hexworks.microcline.fragments.ToolBelt
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.mvc.base.BaseView


class DrawView(context: EditorContext) : BaseView() {

    override val theme = Config.THEME

    val drawArea: DrawArea by lazy {
        DrawArea(
                position = Positions.defaultPosition(),
                context = context)
    }

    private val toolBelt: ToolBelt by lazy {
        ToolBelt(
                screen = screen,
                position = Positions.bottomLeftOf(drawArea.root),
                context = context)
    }

    override fun onDock() {
        screen.addFragment(drawArea)
        screen.addFragment(toolBelt)
    }

}
