package org.hexworks.microcline.dialogs

import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.DrawTools
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.onSelection
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class ModeSelectorDialog(screen: Screen,
                         private val context: EditorContext) : BaseDialog(screen) {

    override val content = Components.panel()
            .withSize(20, TOOL_COUNT + 5)
            .withDecorations(box(boxType = BoxType.DOUBLE, title = "Draw Mode"))
            .build().apply {
                addComponent(Components.radioButtonGroup()
                        .withPosition(Position.offset1x1())
                        .withSize(17, TOOL_COUNT)
                        .build().apply {
                            onSelection { selected ->
                                // TODO: use service
                                context.currentTool = DrawTools.valueOf(selected.key).drawTool
                            }
                            DrawTools.values().map { mode ->
                                addOption(mode.toString(), mode.drawTool.name).apply {
                                    if (context.currentTool == mode.drawTool) {
                                        isSelected = true
                                    }
                                }
                            }
                        })
            }

    companion object {
        val TOOL_COUNT = DrawTools.values().size
    }
}
