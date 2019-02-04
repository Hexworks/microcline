package org.hexworks.microcline.components

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.views.dialogs.FileSelectorDialog
import org.hexworks.microcline.views.dialogs.LayerSelectorDialog
import org.hexworks.microcline.views.dialogs.ModeSelectorDialog
import org.hexworks.microcline.views.dialogs.TileSelectorDialog
import org.hexworks.microcline.drawers.Drawer
import org.hexworks.microcline.data.events.DrawModeChanged
import org.hexworks.microcline.data.events.LayerSelected
import org.hexworks.microcline.data.events.MousePosition
import org.hexworks.microcline.data.events.TileChanged
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class ToolBelt(screen: Screen,
               position: Position,
               val panel: Panel = Components.panel()
                       .wrapWithBox(true)
                       .withSize(Size.create(Config.WINDOW_WIDTH, Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
                       .withPosition(position)
                       .build()
) {

    // Selected tile must be wrapped in a NoOpComponentRenderer() panel to be displayed correctly.
    private val tilePanel = Components.panel()
            .withSize(Size.one())
            .withComponentRenderer(NoOpComponentRenderer())
            .build()

    private val modeText = Components.textArea()
            .withSize(Size.create(9, 1))
            .build().apply { disable() }
    private val layerText = Components.textArea()
            .withSize(Size.create(12, 1))
            .build().apply { disable() }
    private val fileText = Components.textArea()
            .withSize(Size.create(18, 1))
            .build().apply { disable() }
    private val xCoordText = Components.textArea()
            .withSize(Size.create(2, 1))
            .build().apply { disable() }
    private val yCoordText = Components.textArea()
            .withSize(Size.create(2, 1))
            .build().apply { disable() }

    init {
        val tileTool = Tool(
                Position.zero(),
                "Tile",
                tilePanel,
                { action: MouseAction ->
                    screen.openModal(TileSelectorDialog(screen))
                }
        )
        val modeTool = Tool(
                Position.topRightOf(tileTool.wrapper),
                "Mode",
                modeText,
                { action: MouseAction ->
                    screen.openModal(ModeSelectorDialog(screen))
                }
        )
        val layerTool = Tool(
                Position.topRightOf(modeTool.wrapper),
                "Layer",
                layerText,
                { action: MouseAction ->
                    screen.openModal(LayerSelectorDialog(screen))
                }
        )
        val fileTool = Tool(
                Position.topRightOf(layerTool.wrapper),
                "File",
                fileText,
                { action: MouseAction ->
                    screen.openModal(FileSelectorDialog(screen))
                }
        )
        val xCoordTool = Tool(
                Position.topRightOf(fileTool.wrapper),
                "X",
                xCoordText,
                { action: MouseAction -> }
        )
        val yCoordTool = Tool(
                Position.topRightOf(xCoordTool.wrapper),
                "Y",
                yCoordText,
                { action: MouseAction -> }
        )

        panel.addComponent(tileTool.wrapper)
        panel.addComponent(modeTool.wrapper)
        panel.addComponent(layerTool.wrapper)
        panel.addComponent(fileTool.wrapper)
        panel.addComponent(xCoordTool.wrapper)
        panel.addComponent(yCoordTool.wrapper)

        // Init selectors.
        updateTile(State.tile)
        updateMode(State.drawer)
        updateLayer(State.layerRegistry.selected.get().labelProperty.value)

        // Event subscriptions.
        Zircon.eventBus.subscribe<MousePosition> {
            updateCoords(it.x, it.y)
        }
        Zircon.eventBus.subscribe<DrawModeChanged> {
            updateMode(it.mode)
        }
        Zircon.eventBus.subscribe<TileChanged> {
            updateTile(it.tile)
        }
        Zircon.eventBus.subscribe<LayerSelected> {
            updateLayer(it.layer.labelProperty.value)
        }
    }

    fun updateTile(tile: Tile) {
        tilePanel.setTileAt(Position.zero(), tile.withModifiers(Modifiers.border()))
    }

    fun updateMode(mode: Drawer) {
        modeText.text = mode.name()
    }

    fun updateLayer(layer: String) {
        layerText.text = layer
    }

    fun updateFile(file: String) {
        fileText.text = file
    }

    fun updateCoords(x: Int, y: Int) {
        xCoordText.text = x.toString()
        yCoordText.text = y.toString()
    }
}
