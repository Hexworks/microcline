package org.hexworks.microcline.components.dialogs

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.data.events.LayerOrderChanged
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.kotlin.onMouseReleased
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.Zircon


class LayerSelectorDialog(screen: Screen) : Dialog(screen) {

    private val layerList = Components.panel()
            .withPosition(Position.zero())
            .withSize(23,10)
            .build().apply {
                rebuildList(this)

                Zircon.eventBus.subscribe<LayerOrderChanged> {
                    // If its just a layer visibility change, we don't have to rebuild the list.
                    if (!it.visibility) {
                        rebuildList(this)
                    }
                }
            }

    override val container = Components.panel()
            .withTitle("Layers")
            .withSize(25,15)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {
                addComponent(layerList)
            }

    private fun rebuildList(container: Container) {
        container.detachAllComponents()
        State.layerRegistry.layerHandlers().forEach {
            container.addComponent(it.panel)
        }
        // TODO: lift the size constraint once we can scroll lists in Zircon.
        if (State.layerRegistry.layers().size < 10) {
            container.addComponent(Components.button().withText("Add layer")
                    .withAlignmentWithin(container, ComponentAlignment.BOTTOM_CENTER)
                    .build().apply {
                        onMouseReleased {
                            State.layerRegistry.create()
                        }
                    })
        }
        // TODO: this must be a bug in Zircon.
        container.applyColorTheme(ColorThemes.amigaOs())
    }

}
