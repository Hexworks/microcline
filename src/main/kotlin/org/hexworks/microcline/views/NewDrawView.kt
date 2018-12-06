package org.hexworks.microcline.views

import org.hexworks.microcline.components.DrawArea
import org.hexworks.microcline.components.ToolBelt
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.grid.TileGrid


class NewDrawView(tileGrid: TileGrid, state: State) : BaseView(tileGrid) {

    init {
        val drawArea = DrawArea(Positions.defaultPosition(), state)
        val toolBelt = ToolBelt(screen, Positions.bottomLeftOf(drawArea.wrapper), state)

        screen.addComponent(drawArea.wrapper)
        screen.addComponent(toolBelt.panel)
    }
}
