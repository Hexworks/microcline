package org.hexworks.microcline.components.dialogs

import org.hexworks.microcline.data.DrawTools
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onSelection
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class ModeSelectorDialog(screen: Screen) : BaseDialog(screen) {

    override val content = Components.panel()
            .withTitle("DrawTool Mode")
            .withSize(17, DrawTools.values().size + 5)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().also { panel ->
                val options = Components.radioButtonGroup()
                        .withPosition(Position.offset1x1())
                        .withSize(13, DrawTools.values().size)
                        .build()

                DrawTools.values().map { mode ->
                    options.addOption(mode.toString(), mode.drawTool.name).also { option ->
                        if (State.drawTool == mode.drawTool) {
                            option.isSelected = true
                        }
                    }
                }

                options.onSelection { selected ->
                    State.drawTool = DrawTools.valueOf(selected.key).drawTool
                }

                panel.addComponent(options)
            }

}
