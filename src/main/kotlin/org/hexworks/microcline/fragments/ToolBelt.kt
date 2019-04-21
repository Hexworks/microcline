package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.extensions.bind
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.dialogs.FileSelectorDialog
import org.hexworks.microcline.dialogs.LayerSelectorDialog
import org.hexworks.microcline.dialogs.ModeSelectorDialog
import org.hexworks.microcline.dialogs.TileSelectorDialog
import org.hexworks.microcline.events.LayerSelected
import org.hexworks.microcline.extensions.onMouseEvent
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_MOVED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase.TARGET
import org.hexworks.zircon.internal.Zircon

// TODO: use services
class ToolBelt(screen: Screen,
               position: Position,
               private val context: EditorContext) : Fragment {

    override val root = Components.panel()
            .withSize(Size.create(
                    width = Config.WINDOW_WIDTH,
                    height = Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
            .withPosition(position)
            .build()

    private val selectedTileIcon = Components.icon()
            .withIcon(context.selectedTile)
            .withTileset(screen.currentTileset())
            .build().apply {
                iconProperty.bind(context.selectedTileProperty)
            }

    private val modeText = Components.label()
            .withSize(Size.create(9, 1))
            .withText(context.currentTool.name)
            .build().apply {
                textProperty.bind(context.currentToolProperty) {
                    it.name
                }
            }

    private val layerText = Components.label()
            .withSize(Size.create(8, 1))
            .build()

    private val fileText = Components.label()
            .withSize(Size.create(10, 1))
            .withText(Config.NONAME_FILE)
            .build().apply {
                textProperty.bind(context.currentFileProperty) {
                    it.map { file -> file.name }.orElse(Config.NONAME_FILE)
                }
            }

    private val tileTool = Tool(
            position = Position.zero(),
            buttonLabel = "Tile",
            visualization = selectedTileIcon,
            activationHandler = { screen.openModal(TileSelectorDialog(screen, context)) })

    private val modeTool = Tool(
            position = Position.topRightOf(tileTool.root),
            buttonLabel = "Mode",
            visualization = modeText,
            activationHandler = { screen.openModal(ModeSelectorDialog(screen, context)) })

    private val layerTool = Tool(
            position = Position.topRightOf(modeTool.root),
            buttonLabel = "Layer",
            visualization = layerText,
            activationHandler = { screen.openModal(LayerSelectorDialog(screen, context)) })

    private val fileTool = Tool(
            position = Position.topRightOf(layerTool.root),
            buttonLabel = "File",
            visualization = fileText,
            activationHandler = { screen.openModal(FileSelectorDialog(screen, context)) })

    private val positionText = Components.label()
            .withSize(Size.create(13, 3))
            .wrapWithBox(true)
            .withAlignmentWithin(root, ComponentAlignment.BOTTOM_RIGHT)
            .withText("X: 0, Y: 0")
            .build()

    init {
        root.addFragment(tileTool)
        root.addFragment(modeTool)
        root.addFragment(layerTool)
        root.addFragment(fileTool)
        root.addComponent(positionText)

        // Init selectors.
        updateLayer(context.layerRegistry.selected.get().labelProperty.value)

        // Event subscriptions.
        Zircon.eventBus.subscribe<LayerSelected> {
            updateLayer(it.layer.labelProperty.value)
        }
        screen.onMouseEvent(MOUSE_MOVED, TARGET) {
            val pos = it.position - Positions.offset1x1()
            positionText.text = "X: ${pos.x} Y: ${pos.y}"
            Processed
        }
    }

    private fun updateLayer(layer: String) {
        layerText.text = layer
    }

}
