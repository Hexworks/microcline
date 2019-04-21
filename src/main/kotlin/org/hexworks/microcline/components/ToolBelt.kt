package org.hexworks.microcline.components

import org.hexworks.cobalt.databinding.api.extensions.bind
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.components.dialogs.FileSelectorDialog
import org.hexworks.microcline.components.dialogs.LayerSelectorDialog
import org.hexworks.microcline.components.dialogs.ModeSelectorDialog
import org.hexworks.microcline.components.dialogs.TileSelectorDialog
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.events.LayerSelected
import org.hexworks.microcline.extensions.onMouseEvent
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_MOVED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase.TARGET
import org.hexworks.zircon.internal.Zircon

// TODO: use services
// TODO: make this a fragment
class ToolBelt(screen: Screen,
               position: Position,
               private val context: EditorContext,
               val panel: Panel = Components.panel()
                       .wrapWithBox(true)
                       .withSize(Size.create(Config.WINDOW_WIDTH, Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
                       .withPosition(position)
                       .build()) {

    private val selectedTileIcon = Components.icon()
            .withIcon(context.selectedTile)
            .withTileset(screen.currentTileset())
            .build().apply {
                iconProperty.bind(context.selectedTileProperty)
            }

    private val modeText = Components.button()
            .withSize(Size.create(9, 1))
            .withText(context.currentTool.name)
            .wrapSides(false)
            .build().apply {
                textProperty.bind(context.currentToolProperty) {
                    it.name
                }
            }

    private val layerText = Components.textArea()
            .withSize(Size.create(12, 1))
            .build().apply { disable() }

    private val fileText = Components.button()
            .withSize(Size.create(18, 1))
            .withText(Config.NONAME_FILE)
            .wrapSides(false)
            .build().apply {
                textProperty.bind(context.currentFileProperty) {
                    it.map { file -> file.name }.orElse(Config.NONAME_FILE)
                }
            }

    private val xPosText = Components.label()
            .withSize(Size.create(2, 1))
            .withText("0")
            .build()

    private val yPosText = Components.label()
            .withSize(Size.create(2, 1))
            .withText("0")
            .build()

    init {
        val tileTool = Tool(
                position = Position.zero(),
                labelText = "Tile",
                component = selectedTileIcon,
                clickHandler = { screen.openModal(TileSelectorDialog(screen, context)) })

        val modeTool = Tool(
                position = Position.topRightOf(tileTool.root),
                labelText = "Mode",
                component = modeText,
                clickHandler = { screen.openModal(ModeSelectorDialog(screen, context)) })

        val layerTool = Tool(
                position = Position.topRightOf(modeTool.root),
                labelText = "Layer",
                component = layerText,
                clickHandler = { screen.openModal(LayerSelectorDialog(screen, context)) })

        val fileTool = Tool(
                position = Position.topRightOf(layerTool.root),
                labelText = "File",
                component = fileText,
                clickHandler = { screen.openModal(FileSelectorDialog(screen, context)) })

        val xPosTool = Tool(
                position = Position.topRightOf(fileTool.root),
                labelText = "X",
                component = xPosText)

        val yPosTool = Tool(
                position = Position.topRightOf(xPosTool.root),
                labelText = "Y",
                component = yPosText)

        panel.addFragment(tileTool)
        panel.addFragment(modeTool)
        panel.addFragment(layerTool)
        panel.addFragment(fileTool)
        panel.addFragment(xPosTool)
        panel.addFragment(yPosTool)

        // Init selectors.
        updateLayer(context.layerRegistry.selected.get().labelProperty.value)

        // Event subscriptions.
        Zircon.eventBus.subscribe<LayerSelected> {
            updateLayer(it.layer.labelProperty.value)
        }
        screen.onMouseEvent(MOUSE_MOVED, TARGET) {
            val pos = it.position - Positions.offset1x1()
            xPosText.text = pos.x.toString()
            yPosText.text = pos.y.toString()
            Processed
        }
    }

    private fun updateLayer(layer: String) {
        layerText.text = layer
    }

}
