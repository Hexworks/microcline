package org.hexworks.microcline.panels

import org.hexworks.microcline.common.DrawMode
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.component.RadioButtonGroup
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.internal.component.impl.DefaultRadioButton
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer

const val TOOLS_P_SIZE_X = 16
const val TOOLS_P_SIZE_Y = 7


class ToolsPanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .withTitle("Tools")
                .withSize(Sizes.create(TOOLS_P_SIZE_X, TOOLS_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .withPosition(position)
                .withComponentRenderer(NoOpComponentRenderer())
                .build(),
        private val toolOptions: RadioButtonGroup = Components.radioButtonGroup()
                .withSize(Sizes.create(TOOLS_P_SIZE_X, TOOLS_P_SIZE_Y))
                .withPosition(Positions.defaultPosition())
                .build()
): Panel by panel {


    init {
        // TODO: add select() to RadioButton(Group?) interface in Zircon, so we can get rid of the casting here
        (listOf(
                DrawMode.FREE,
                DrawMode.LINE,
                DrawMode.RECTANGLE,
                DrawMode.OVAL,
                DrawMode.FILL,
                DrawMode.TEXT,
                DrawMode.SELECT
        ).map {
            toolOptions.addOption(it, it.capitalize())
        }.first() as DefaultRadioButton).select()

        this.addComponent(toolOptions)
    }

    fun getPanel() = panel

    fun selectedMode() = toolOptions.fetchSelectedOption().get()

}
