package org.hexworks.microcline.dialogs

import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.DrawTools
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onSelection
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class ModeSelectorDialog(screen: Screen,
                         private val context: EditorContext) : BaseDialog(screen) {

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
                        if (context.currentTool == mode.drawTool) {
                            option.isSelected = true
                        }
                    }
                }

                options.onSelection { selected ->
                    // TODO: use service
                    context.currentTool = DrawTools.valueOf(selected.key).drawTool
                }

                panel.addComponent(options)
            }

}
