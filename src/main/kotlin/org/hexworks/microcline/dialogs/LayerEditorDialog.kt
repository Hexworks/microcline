package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.events.LayerChanged
import org.hexworks.microcline.fragments.LayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.ComponentAlignment.BOTTOM_LEFT
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon


class LayerEditorDialog(screen: Screen,
                        private val context: EditorContext) : BaseDialog(screen) {

    private val editorsPanel: Panel

    override val content = Components.panel()
            .withTitle("Edit Layers")
            .withSize(52, 20)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {
                val name = Components.header().withSize(9, 1).withText("Name").build()
                val select = Components.header().withPosition(Positions.topRightOf(name)).withSize(7, 1).withText("Select").build()
                val visible = Components.header().withPosition(Positions.topRightOf(select)).withSize(7, 1).withText("Visible").build()
                val lock = Components.header().withPosition(Positions.topRightOf(visible)).withSize(6, 1).withText("Lock").build()
                val move = Components.header().withPosition(Positions.topRightOf(lock)).withSize(6, 1).withText("Move").build()
                editorsPanel = Components.panel().withSize(50, 10).withPosition(Positions.bottomLeftOf(name)).build()
                val new = Components.button()
                        .withAlignmentWithin(this, BOTTOM_LEFT)
                        .withSize(11, 1)
                        .withText("New Layer")
                        .build().apply {
                            onComponentEvent(ACTIVATED) {
                                context.drawLayerEditor.addNewLayer()
                                refreshLayerEditors()
                                Processed
                            }
                        }
                addComponent(name)
                addComponent(select)
                addComponent(visible)
                addComponent(lock)
                addComponent(move)
                addComponent(new)
                addComponent(editorsPanel)
                refreshLayerEditors()
            }

    init {
        Zircon.eventBus.subscribe<LayerChanged> {
            refreshLayerEditors()
        }
    }

    private fun refreshLayerEditors() {
        editorsPanel.clear()
        context.currentLayers.forEachIndexed { idx, layer ->
            editorsPanel.addFragment(LayerEditor(
                    position = Positions.create(0, idx + 1),
                    layer = layer,
                    context = context))
        }
        editorsPanel.applyColorTheme(Config.THEME)
    }


}
