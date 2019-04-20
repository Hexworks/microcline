package org.hexworks.microcline.views

import org.hexworks.microcline.components.DrawArea
import org.hexworks.microcline.components.ToolBelt
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.mvc.base.BaseView


class DrawView : BaseView() {

    override val theme = ColorThemes.amigaOs()

    override fun onDock() {
        val drawArea = DrawArea(Positions.defaultPosition())
        val toolBelt = ToolBelt(screen, Positions.bottomLeftOf(drawArea.panel))

        screen.addComponent(drawArea.panel)
        screen.addComponent(toolBelt.panel)
    }
}
