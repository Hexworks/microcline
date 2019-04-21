package org.hexworks.microcline.views

import org.hexworks.microcline.components.DrawArea
import org.hexworks.microcline.components.ToolBelt
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.mvc.base.BaseView


class DrawView(private val context: EditorContext) : BaseView() {

    override val theme = Config.THEME

    override fun onDock() {
        val drawArea = DrawArea(Positions.defaultPosition(), context)
        val toolBelt = ToolBelt(screen, Positions.bottomLeftOf(drawArea.panel), context)

        screen.addComponent(drawArea.panel)
        screen.addComponent(toolBelt.panel)
    }
}
