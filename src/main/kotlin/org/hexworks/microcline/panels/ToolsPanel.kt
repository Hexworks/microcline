package org.hexworks.microcline.panels

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position

const val TOOLS_P_SIZE_X = 16
const val TOOLS_P_SIZE_Y = 7


class ToolsPanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .title("Tools")
                .size(Sizes.create(TOOLS_P_SIZE_X, TOOLS_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .position(position)
                .build()
): Panel by panel {

    init {
        val toolOptions = Components.radioButtonGroup()
                .size(Sizes.create(TOOLS_P_SIZE_X, TOOLS_P_SIZE_Y))
                .position(Positions.defaultPosition())
                .build()
        toolOptions.addOption("select", "Select")
        toolOptions.addOption("cell", "Cell")
        toolOptions.addOption("line", "Line")
        toolOptions.addOption("rectangle", "Rectangle")
        toolOptions.addOption("oval", "Oval")
        toolOptions.addOption("fill", "Fill")
        toolOptions.addOption("text", "Text")
        this.addComponent(toolOptions)
    }

    fun getPanel() = panel

}
