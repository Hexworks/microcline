package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.commands.CreateLayer
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.dialogs.base.BaseDialog
import org.hexworks.microcline.events.EditorRedrawRequested
import org.hexworks.microcline.events.LayerCreated
import org.hexworks.microcline.extensions.DrawLayersArea
import org.hexworks.microcline.fragments.LayerEditorFragment
import org.hexworks.microcline.model.DrawLayerModel
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.ComponentAlignment.CENTER
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.component.modal.EmptyModalResult


class LayerEditorDialog(
        screen: Screen,
        private val drawing: Drawing,
        private val drawLayersArea: DrawLayersArea) : BaseDialog<EmptyModalResult>(screen, EDITOR_SIZE, "Edit Layers") {

    private val layerEditors = Components.vbox().withSize(EDITOR_SIZE.withRelativeHeight(-1)).build()
    private val models = mutableListOf<DrawLayerModel>()

    override val content = Components.vbox()
            .withSize(EDITOR_SIZE)
            .build().apply {
                addComponent(Components.hbox().withSpacing(1).withSize(EDITOR_WIDTH, 1).build().apply {
                    addComponent(Components.header().withSize(NAME_FIELD_WIDTH, 1).withText("Name").build())
                    addComponent(Components.header().withSize(SELECT_FIELD_WIDTH, 1).withText("Select").build())
                    addComponent(Components.header().withSize(VISIBLE_FIELD_WIDTH, 1).withText("Visible").build())
                    addComponent(Components.header().withSize(LOCK_FIELD_WIDTH, 1).withText("Lock").build())
                    addComponent(Components.header().withSize(MOVE_FIELD_WIDTH, 1).withText("Move").build())
                })
                addComponent(layerEditors)
                refreshLayerEditors()
            }

    init {
        Zircon.eventBus.subscribe<EditorRedrawRequested> {
            refreshLayerEditors()
        }
        buttonBar.addComponent(Components.button()
                .withText("New Layer")
                .build().apply {
                    processComponentEvents(ACTIVATED) {
                        createLayer()
                    }
                })
        buttonBar.addComponent(Components.button()
                .withText("Close")
                .withAlignmentWithin(buttonBar, CENTER)
                .build().apply {
                    processComponentEvents(ACTIVATED) {
                        root.close(EmptyModalResult)
                    }
                })
    }

    // TODO: figure out how to do MVC properly
    private fun createLayer() {
        drawing.execute(CreateLayer())
        Zircon.eventBus.publish(LayerCreated(drawing.state.layers.last()))
        refreshLayerEditors()
    }

    private fun refreshLayerEditors() {
        layerEditors.clear()
        models.clear()
        drawing.state.layers.forEachIndexed { index, layer ->
            val model = DrawLayerModel(layer, index)
            models.add(model)
            val fragment = LayerEditorFragment(
                    width = EDITOR_WIDTH - 2,
                    layerModel = model,
                    drawing = drawing)
            fragment.onSelected {
                deselectOtherLayers(index)
            }
            layerEditors.addFragment(fragment)
        }
        layerEditors.applyColorTheme(Config.THEME)
    }

    private fun deselectOtherLayers(index: Int) {
        models.filterIndexed { idx, _ ->
            index != idx
        }.forEach {
            it.isSelected = false
        }
    }

    companion object {

        private const val EDITOR_WIDTH = 60
        private const val EDITOR_HEIGHT = 20

        private val EDITOR_SIZE = Sizes.create(EDITOR_WIDTH, EDITOR_HEIGHT)

        const val NAME_FIELD_WIDTH = 10
        const val SELECT_FIELD_WIDTH = 7
        const val VISIBLE_FIELD_WIDTH = 7
        const val LOCK_FIELD_WIDTH = 6
        const val MOVE_FIELD_WIDTH = 6
    }
}
