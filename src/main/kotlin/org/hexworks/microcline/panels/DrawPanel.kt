package org.hexworks.microcline.panels

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class DrawPanel(
        position: Position,
        size: Size,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .withTitle("Draw")
                .withSize(size)
                .withPosition(position)
                .withComponentRenderer(NoOpComponentRenderer())
                .build()
): Panel by panel {

    fun getPanel() = panel

}
