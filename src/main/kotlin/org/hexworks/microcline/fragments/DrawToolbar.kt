package org.hexworks.microcline.fragments

import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.microcline.commands.SelectTile
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.ApplicationContext
import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.dialogs.LayerEditorDialog
import org.hexworks.microcline.dialogs.ModeSelectorDialog
import org.hexworks.microcline.dialogs.TileSelectorDialog
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.onClosed
import org.hexworks.zircon.api.screen.Screen


class DrawToolbar(screen: Screen,
                  private val drawing: Drawing,
                  private val context: ApplicationContext) : Fragment {

    override val root = Components.hbox()
            .withSize(Size.create(
                    width = Config.WINDOW_WIDTH,
                    height = Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
            .build()

    private val selectedTileIcon = Components.icon()
            .withIcon(context.selectedTileProperty.value)
            .withTileset(screen.currentTileset())
            .build().apply {
                iconProperty.bind(context.selectedTileProperty)
            }

    private val modeText = Components.label()
            .withSize(Size.create(9, 1))
            .withText(context.selectedDrawMode.label)
            .build().apply {
                textProperty.updateFrom(context.selectedDrawModeProperty) {
                    it.label
                }
            }

    private val layerText = Components.label()
            .withSize(Size.create(28, 1))
            .build().apply {
                textProperty.updateFrom(context.selectedLayerProperty) {
                    it.name
                }
            }

    private val tileTool = Tool(
            buttonLabel = "Tile",
            visualization = selectedTileIcon,
            activationHandler = {
                val modal = TileSelectorDialog(screen, context.selectedTile)
                modal.root.onClosed {
                    it.result.map { selectedTile ->
                        context.selectedTile = selectedTile
                        drawing.execute(SelectTile(selectedTile))
                    }
                }
                screen.openModal(modal)
            })

    private val modeTool = Tool(
            buttonLabel = "Mode",
            visualization = modeText,
            activationHandler = {
                val dialog = ModeSelectorDialog(screen, context.selectedDrawMode)
                dialog.root.onClosed {
                    it.result.map { drawMode ->
                        context.selectedDrawMode = drawMode
                    }
                }
                screen.openModal(dialog)
            })

    private val layerTool = Tool(
            buttonLabel = "Layer",
            visualization = layerText,
            activationHandler = {
                screen.openModal(LayerEditorDialog(
                        screen = screen,
                        drawing = drawing,
                        drawLayersArea = context.drawLayersArea))
            })

    private val mousePosition = Components.label()
            .withSize(Size.create(13, 3))
            .withDecorations(box())
            .withText("X: 0, Y: 0")
            .build()

    init {
        root.addFragment(tileTool)
        root.addFragment(modeTool)
        root.addFragment(layerTool)
        root.addComponent(mousePosition)
    }

    fun updateMousePosition(pos: Position) {
        mousePosition.text = "X: ${pos.x} Y: ${pos.y}"
    }
}
