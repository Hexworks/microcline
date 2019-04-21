package org.hexworks.microcline.components

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.components.dialogs.FileSelectorDialog
import org.hexworks.microcline.components.dialogs.LayerSelectorDialog
import org.hexworks.microcline.components.dialogs.ModeSelectorDialog
import org.hexworks.microcline.components.dialogs.TileSelectorDialog
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.data.events.DrawModeChanged
import org.hexworks.microcline.data.events.FileChanged
import org.hexworks.microcline.data.events.LayerSelected
import org.hexworks.microcline.data.events.MousePosition
import org.hexworks.microcline.data.events.TileChanged
import org.hexworks.microcline.drawtools.DrawTool
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Modifiers
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.Zircon
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class ToolBelt(screen: Screen,
               position: Position,
               val panel: Panel = Components.panel()
                       .wrapWithBox(true)
                       .withSize(Size.create(Config.WINDOW_WIDTH, Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
                       .withPosition(position)
                       .build()) {

    // Selected tile must be wrapped into a NoOpComponentRenderer() panel to be displayed correctly.
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
    private val xPosText = Components.textArea()
            .withSize(Size.create(2, 1))
            .build().apply { disable() }
    private val yPosText = Components.textArea()
            .withSize(Size.create(2, 1))
            .build().apply { disable() }

    init {
        val tileTool = Tool(
                Position.zero(),
                "Tile",
                tilePanel,
                { screen.openModal(TileSelectorDialog(screen)) }
        )
        val modeTool = Tool(
                Position.topRightOf(tileTool.wrapper),
                "Mode",
                modeText,
                { screen.openModal(ModeSelectorDialog(screen)) }
        )
        val layerTool = Tool(
                Position.topRightOf(modeTool.wrapper),
                "Layer",
                layerText,
                { screen.openModal(LayerSelectorDialog(screen)) }
        )
        val fileTool = Tool(
                Position.topRightOf(layerTool.wrapper),
                "File",
                fileText,
                { screen.openModal(FileSelectorDialog(screen)) }
        )
        val xPosTool = Tool(
                Position.topRightOf(fileTool.wrapper),
                "X",
                xPosText,
                { }
        )
        val yPosTool = Tool(
                Position.topRightOf(xPosTool.wrapper),
                "Y",
                yPosText,
                { }
        )

        panel.addComponent(tileTool.wrapper)
        panel.addComponent(modeTool.wrapper)
        panel.addComponent(layerTool.wrapper)
        panel.addComponent(fileTool.wrapper)
        panel.addComponent(xPosTool.wrapper)
        panel.addComponent(yPosTool.wrapper)

        // Init selectors.
        updateTile(State.tile)
        updateMode(State.drawTool)
        updateLayer(State.layerRegistry.selected.get().labelProperty.value)
        updateFile(Config.NONAME_FILE)

        // Event subscriptions.
        Zircon.eventBus.subscribe<TileChanged> {
            updateTile(it.tile)
        }
        Zircon.eventBus.subscribe<DrawModeChanged> {
            updateMode(it.mode)
        }
        Zircon.eventBus.subscribe<LayerSelected> {
            updateLayer(it.layer.labelProperty.value)
        }
        Zircon.eventBus.subscribe<FileChanged> {
            updateFile(it.file)
        }
        Zircon.eventBus.subscribe<MousePosition> {
            updatePos(it.x, it.y)
        }
    }

    private fun updateTile(tile: Tile) {
        tilePanel.setTileAt(Position.zero(), tile.withModifiers(Modifiers.border()))
    }

    private fun updateMode(mode: DrawTool) {
        modeText.text = mode.name
    }

    private fun updateLayer(layer: String) {
        layerText.text = layer
    }

    private fun updateFile(file: String) {
        fileText.text = file
    }

    private fun updatePos(x: Int, y: Int) {
        xPosText.text = x.toString()
        yPosText.text = y.toString()
    }

}
