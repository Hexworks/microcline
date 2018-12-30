package org.hexworks.microcline.components

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.config.NewConfig
import org.hexworks.microcline.dialogs.FileSelectorDialog
import org.hexworks.microcline.dialogs.LayerSelectorDialog
import org.hexworks.microcline.dialogs.ModeSelectorDialog
import org.hexworks.microcline.dialogs.TileSelectorDialog
import org.hexworks.microcline.events.DrawModeChanged
import org.hexworks.microcline.events.MousePosition
import org.hexworks.microcline.events.TileChanged
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
               state: State,
               val panel: Panel = Components.panel()
                       .wrapWithBox(true)
                       .withSize(Size.create(NewConfig.WINDOW_WIDTH, NewConfig.TOOLBELT_HEIGHT + 2 * NewConfig.BORDER_SIZE))
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
                    screen.openModal(TileSelectorDialog(screen, state))
                }
        )
        val modeTool = Tool(
                Position.topRightOf(tileTool.wrapper),
                "Mode",
                modeText,
                { action: MouseAction ->
                    screen.openModal(ModeSelectorDialog(screen, state))
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
        updateTile(state.tile)
        updateMode(state.mode)

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
    }

    fun updateTile(tile: Tile) {
        tilePanel.setTileAt(Position.zero(), tile.withModifiers(Modifiers.border()))
    }

    fun updateMode(mode: DrawMode) {
        modeText.text = mode.label
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