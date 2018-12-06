package org.hexworks.microcline.dialogs

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.kotlin.onSelection
import org.hexworks.zircon.api.screen.Screen


class ModeSelectorDialog(screen: Screen, private val state: State) : Dialog(screen) {

    override val container = Components.panel()
            .withTitle("Draw Mode")
            .withSize(17, DrawMode.values().size + 5)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().also { panel ->
                val options = Components.radioButtonGroup()
                        .withPosition(Position.offset1x1())
                        .withSize(13, DrawMode.values().size)
                        .build()

                enumValues<DrawMode>().asList().map { mode ->
                    options.addOption(mode.toString(), mode.label).also { option ->
                        if (state.mode == mode) {
                            option.select()
                        }
                    }
                }

                options.onSelection { selected ->
                    state.mode = DrawMode.valueOf(selected.key)
                }

                panel.addComponent(options)
            }

}
