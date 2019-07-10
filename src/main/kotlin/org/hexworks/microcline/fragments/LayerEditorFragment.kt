package org.hexworks.microcline.fragments

import org.hexworks.microcline.commands.ClearLayer
import org.hexworks.microcline.commands.DeleteLayer
import org.hexworks.microcline.commands.MoveLayerDown
import org.hexworks.microcline.commands.MoveLayerUp
import org.hexworks.microcline.commands.SelectLayer
import org.hexworks.microcline.commands.SetLayerLockState
import org.hexworks.microcline.commands.SetLayerVisibility
import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.dialogs.LayerEditorDialog
import org.hexworks.microcline.events.EditorRedrawRequested
import org.hexworks.microcline.events.LayerCleared
import org.hexworks.microcline.events.LayerDeleted
import org.hexworks.microcline.events.LayerMovedDown
import org.hexworks.microcline.events.LayerMovedUp
import org.hexworks.microcline.events.LayerSelected
import org.hexworks.microcline.events.LayerVisibilityChanged
import org.hexworks.microcline.model.DrawLayerModel
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.uievent.ComponentEventType
import org.hexworks.zircon.internal.Zircon


class LayerEditorFragment(
        val drawing: Drawing,
        var layerModel: DrawLayerModel,
        width: Int) : Fragment {

    private val upButton = Components.button()
            .withText("${Symbols.ARROW_UP}")
            .build().apply {
                isDisabled = layerModel.index == 0
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    if(isDisabled.not()) {
                        drawing.execute(MoveLayerUp(layerModel.index))
                        Zircon.eventBus.publish(EditorRedrawRequested)
                        Zircon.eventBus.publish(LayerMovedUp(layerModel.index - 1))
                    }
                }
            }

    private val downButton = Components.button()
            .withText("${Symbols.ARROW_DOWN}")
            .build().apply {
                isDisabled = layerModel.index == drawing.state.layers.lastIndex
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    if(isDisabled.not()) {
                        drawing.execute(MoveLayerDown(layerModel.index))
                        Zircon.eventBus.publish(EditorRedrawRequested)
                        Zircon.eventBus.publish(LayerMovedDown(layerModel.index + 1))
                    }
                }
            }

    private val lockButton = Components.checkBox()
            .withSize(LayerEditorDialog.LOCK_FIELD_WIDTH, 1)
            .withText("")
            .build().apply {
                selectedProperty.bind(layerModel.lockedProperty)
                selectedProperty.onChange {
                    drawing.execute(SetLayerLockState(layerModel.index, it.newValue))
                }
            }

    private val selectButton = Components.checkBox()
            .withSize(LayerEditorDialog.SELECT_FIELD_WIDTH, 1)
            .withText("")
            .build().apply {
                selectedProperty.bind(layerModel.selectedProperty)
                selectedProperty.onChange {
                    if (it.newValue) {
                        drawing.execute(SelectLayer(layerModel.index))
                        Zircon.eventBus.publish(LayerSelected(layerModel.index))
                    }
                }
                disabledProperty.updateFrom(selectedProperty)
            }

    private val visibleButton = Components.checkBox()
            .withSize(LayerEditorDialog.VISIBLE_FIELD_WIDTH, 1)
            .withText("")
            .build().apply {
                selectedProperty.bind(layerModel.visibleProperty)
                selectedProperty.onChange {
                    drawing.execute(SetLayerVisibility(layerModel.index, it.newValue))
                    Zircon.eventBus.publish(LayerVisibilityChanged(layerModel.index, it.newValue))
                }
            }

    private val clearButton = Components.button()
            .withText("Clear")
            .build().apply {
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    if (isDisabled.not()) {
                        drawing.execute(ClearLayer(layerModel.index))
                        Zircon.eventBus.publish(LayerCleared(layerModel.index))
                    }
                }
                disabledProperty.updateFrom(lockButton.selectedProperty)
            }

    private val removeButton = Components.button()
            .withText("Remove")
            .build().apply {
                processComponentEvents(ComponentEventType.ACTIVATED) {
                    if (isDisabled.not()) {
                        drawing.execute(DeleteLayer(layerModel.index))
                        Zircon.eventBus.publish(EditorRedrawRequested)
                        Zircon.eventBus.publish(LayerDeleted(layerModel.index))
                    }
                }
                disabledProperty.updateFrom(selectButton.selectedProperty)
            }

    override val root = Components.hbox()
            .withSize(width, 1)
            .withSpacing(1)
            .build().apply {
                addComponent(Components.label()
                        .withSize(LayerEditorDialog.NAME_FIELD_WIDTH, 1)
                        .withText(layerModel.name)
                        .build())
                addComponent(selectButton)
                addComponent(visibleButton)
                addComponent(lockButton)
                addComponent(Components.hbox()
                        .withSize(LayerEditorDialog.MOVE_FIELD_WIDTH, 1)
                        .build().apply {
                            addComponent(upButton)
                            addComponent(downButton)
                        })
                addComponent(clearButton)
                addComponent(removeButton)
            }

    fun onSelected(fn: () -> Unit) {
        layerModel.selectedProperty.onChange {
            if (it.newValue) {
                fn()
            }
        }
    }
}
