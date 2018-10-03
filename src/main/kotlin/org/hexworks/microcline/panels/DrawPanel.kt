package org.hexworks.microcline.panels

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size


class DrawPanel(
        position: Position,
        size: Size,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .title("Draw")
                .size(size)
                .position(position)
                .build()
): Panel by panel {

    fun getPanel() = panel

}
