package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.expression.or
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.dialogs.LayerEditorDialog
import org.hexworks.microcline.events.LayerChanged
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.internal.Zircon


class LayerEditorFragment(val layer: DrawLayer,
                          width: Int,
                          context: EditorContext) : Fragment {

    private val editor = context.drawLayerEditor

    private val upButton = Components.button()
            .withText("${Symbols.ARROW_UP}")
            .build().apply {
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    editor.moveLayerUp(layer)
                    Zircon.eventBus.publish(LayerChanged)
                }
            }

    private val downButton = Components.button()
            .withText("${Symbols.ARROW_DOWN}")
            .build().apply {
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    editor.moveLayerDown(layer)
                    Zircon.eventBus.publish(LayerChanged)
                }
            }

    override val root = Components.hbox()
            .withSize(width, 1)
            .withSpacing(1)
            .build().apply {
                addComponent(Components.label()
                        .withSize(LayerEditorDialog.NAME_FIELD_WIDTH, 1)
                        .withText(layer.nameProperty.value)
                        .build())
                addComponent(Components.checkBox()
                        .withSize(LayerEditorDialog.SELECT_FIELD_WIDTH, 1)
                        .withText("")
                        .build().apply {
                            selectedProperty.bind(layer.selectedProperty)
                            disabledProperty.updateFrom(layer.selectedProperty)
                        })
                addComponent(Components.checkBox()
                        .withSize(LayerEditorDialog.VISIBLE_FIELD_WIDTH, 1)
                        .withText("")
                        .build().apply {
                            selectedProperty.bind(layer.visibleProperty)
                            selectedProperty.onChange {
                                editor.redrawLayers()
                            }
                        })
                addComponent(Components.checkBox()
                        .withSize(LayerEditorDialog.LOCK_FIELD_WIDTH, 1)
                        .withText("")
                        .build().apply {
                            selectedProperty.bind(layer.lockedProperty)
                        })
                addComponent(Components.hbox()
                        .withSize(LayerEditorDialog.MOVE_FIELD_WIDTH, 1)
                        .build().apply {
                            addComponent(upButton)
                            addComponent(downButton)
                        })
                addComponent(Components.button()
                        .withText("Clear")
                        .build().apply {
                            processComponentEvents(ComponentEventType.ACTIVATED) {
                                if (isDisabled.not()) {
                                    layer.clear()
                                    editor.redrawLayers()
                                }
                            }
                            disabledProperty.updateFrom(layer.lockedProperty)
                        })
                addComponent(Components.button()
                        .withText("Remove")
                        .build().apply {
                            processComponentEvents(ComponentEventType.ACTIVATED) {
                                if (isDisabled.not()) {
                                    editor.removeLayer(layer)
                                    Zircon.eventBus.publish(LayerChanged)
                                }
                            }
                            disabledProperty.updateFrom(layer.lockedProperty.or(layer.selectedProperty))
                        })
            }
}
