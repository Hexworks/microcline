package org.hexworks.microcline.components.dialogs

import org.hexworks.microcline.data.Drawers
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onSelection
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class ModeSelectorDialog(screen: Screen) : Dialog(screen) {

    override val container = Components.panel()
            .withTitle("Drawer Mode")
            .withSize(17, Drawers.values().size + 5)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().also { panel ->
                val options = Components.radioButtonGroup()
                        .withPosition(Position.offset1x1())
                        .withSize(13, Drawers.values().size)
                        .build()

                enumValues<Drawers>().asList().map { mode ->
                    options.addOption(mode.toString(), mode.drawer.name()).also { option ->
                        if (State.drawer == mode.drawer) {
                            option.isSelected = true
                        }
                    }
                }

                options.onSelection { selected ->
                    State.drawer = Drawers.valueOf(selected.key).drawer
                }

                panel.addComponent(options)
            }

}
