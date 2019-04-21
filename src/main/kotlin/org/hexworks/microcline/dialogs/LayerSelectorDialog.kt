package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.events.LayerOrderChanged
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon


class LayerSelectorDialog(screen: Screen,
                          private val context: EditorContext) : BaseDialog(screen) {

    private val layerList = Components.panel()
            .withPosition(Position.create(0, 1))
            .withSize(22, 10)
            .build().apply {
                rebuildList(this)

                Zircon.eventBus.subscribe<LayerOrderChanged> {
                    // If its just a layer visibility change, we don't have to rebuild the list.
                    if (!it.visibility) {
                        rebuildList(this)
                    }
                }
            }

    override val content = Components.panel()
            .withTitle("Layers")
            .withSize(24, 15)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {
                addComponent(layerList)
            }

    // TODO: do away with this and use the new concept instead
    private fun rebuildList(container: Container) {
        container.detachAllComponents()
        context.layerRegistry.layerHandlers().forEach {
            container.addFragment(it)
        }
        // TODO: lift the size constraint once we can scroll lists in Zircon.
        if (context.layerRegistry.layers().size < 10) {
            container.addComponent(Components.button().withText("Add layer")
                    .withAlignmentWithin(container, ComponentAlignment.BOTTOM_CENTER)
                    .build().apply {
                        onComponentEvent(ACTIVATED) {
                            context.layerRegistry.create()
                            Processed
                        }
                    })
        }
        // TODO: this must be a bug in Zircon.
        container.applyColorTheme(Config.THEME)
    }

}
