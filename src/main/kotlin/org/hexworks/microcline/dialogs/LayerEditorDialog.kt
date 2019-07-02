package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.events.LayerChanged
import org.hexworks.microcline.fragments.LayerEditorFragment
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.internal.Zircon


class LayerEditorDialog(screen: Screen,
                        private val context: EditorContext) : BaseDialog(screen) {

    private val editor = context.drawLayerEditor
    private val layerEditors: Container

    override val content = Components.panel()
            .withSize(EDITOR_WIDTH, EDITOR_HEIGHT)
            .withDecorations(box(boxType = BoxType.DOUBLE, title = "Edit Layers"))
            .build().apply {
                addComponent(Components.vbox()
                        .withSize(EDITOR_WIDTH - 2, EDITOR_HEIGHT - 3)
                        .build().apply {
                            addComponent(Components.hbox().withSpacing(1).withSize(EDITOR_WIDTH - 2, 1).build().apply {
                                addComponent(Components.header().withSize(NAME_FIELD_WIDTH, 1).withText("Name").build())
                                addComponent(Components.header().withSize(SELECT_FIELD_WIDTH, 1).withText("Select").build())
                                addComponent(Components.header().withSize(VISIBLE_FIELD_WIDTH, 1).withText("Visible").build())
                                addComponent(Components.header().withSize(LOCK_FIELD_WIDTH, 1).withText("Lock").build())
                                addComponent(Components.header().withSize(MOVE_FIELD_WIDTH, 1).withText("Move").build())
                            })
                            layerEditors = Components.vbox().withSize(contentSize.width, contentSize.height - 2).build()
                            addComponent(layerEditors)
                            refreshLayerEditors()
                        })
                addComponent(Components.button()
                        .withText("New Layer")
                        .withAlignmentWithin(this, ComponentAlignment.BOTTOM_LEFT)
                        .build().apply {
                            processComponentEvents(ACTIVATED) {
                                editor.addNewLayer()
                                refreshLayerEditors()
                            }
                        })
            }

    init {
        Zircon.eventBus.subscribe<LayerChanged> {
            refreshLayerEditors()
        }
    }

    private fun refreshLayerEditors() {
        layerEditors.clear()
        context.currentLayers.forEach { layer ->
            layerEditors.addFragment(LayerEditorFragment(
                    width = EDITOR_WIDTH - 2,
                    layer = layer,
                    context = context))
        }
        layerEditors.applyColorTheme(Config.THEME)
    }

    companion object {
        private const val EDITOR_WIDTH = 60
        private const val EDITOR_HEIGHT = 20
        const val NAME_FIELD_WIDTH = 10
        const val SELECT_FIELD_WIDTH = 7
        const val VISIBLE_FIELD_WIDTH = 7
        const val LOCK_FIELD_WIDTH = 6
        const val MOVE_FIELD_WIDTH = 6
    }
}
