package org.hexworks.microcline.fragments

import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.dialogs.FileSelectorDialog
import org.hexworks.microcline.dialogs.LayerEditorDialog
import org.hexworks.microcline.dialogs.ModeSelectorDialog
import org.hexworks.microcline.dialogs.TileSelectorDialog
import org.hexworks.microcline.extensions.processMouseEvents
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_DRAGGED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_MOVED
import org.hexworks.zircon.api.uievent.UIEventPhase.TARGET

class ToolBelt(screen: Screen,
               private val context: EditorContext) : Fragment {

    override val root = Components.hbox()
            .withSize(Size.create(
                    width = Config.WINDOW_WIDTH,
                    height = Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
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
                textProperty.updateFrom(context.currentToolProperty) {
                    it.name
                }
            }

    private val layerText = Components.label()
            .withSize(Size.create(8, 1))
            .build().apply {
                textProperty.updateFrom(context.selectedLayerProperty) {
                    it.name
                }
            }

    private val fileText = Components.label()
            .withSize(Size.create(10, 1))
            .withText(Config.NONAME_FILE)
            .build().apply {
                textProperty.updateFrom(context.currentFileProperty) {
                    it.map { file -> file.name }.orElse(Config.NONAME_FILE)
                }
            }

    private val tileTool = Tool(
            buttonLabel = "Tile",
            visualization = selectedTileIcon,
            activationHandler = { screen.openModal(TileSelectorDialog(screen, context)) })

    private val modeTool = Tool(
            buttonLabel = "Mode",
            visualization = modeText,
            activationHandler = { screen.openModal(ModeSelectorDialog(screen, context)) })

    private val layerTool = Tool(
            buttonLabel = "Layer",
            visualization = layerText,
            activationHandler = { screen.openModal(LayerEditorDialog(screen, context)) })

    private val fileTool = Tool(
            buttonLabel = "File",
            visualization = fileText,
            activationHandler = { screen.openModal(FileSelectorDialog(screen, context)) })

    private val mousePosition = Components.label()
            .withSize(Size.create(13, 3))
            .withDecorations(box())
            .withText("X: 0, Y: 0")
            .build()

    init {
        root.addFragment(tileTool)
        root.addFragment(modeTool)
        root.addFragment(layerTool)
        root.addFragment(fileTool)
        root.addComponent(mousePosition)

        screen.processMouseEvents(MOUSE_MOVED, TARGET) {
            updateMousePosition(it.position)
        }
        screen.processMouseEvents(MOUSE_DRAGGED, TARGET) {
            updateMousePosition(it.position)
        }
    }

    private fun updateMousePosition(pos: Position) {
        val p = pos - Positions.offset1x1()
        mousePosition.text = "X: ${p.x} Y: ${p.y}"
    }
}
