package org.hexworks.microcline.views

import org.hexworks.microcline.controllers.DrawController
import org.hexworks.microcline.controllers.GlyphController
import org.hexworks.microcline.controllers.PaletteController
import org.hexworks.microcline.panels.*
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.input.Input


class DrawView(tileGrid: TileGrid) : View {

    private val screen = Screens.createScreenFor(tileGrid)

    init {
        // Create the panels, top to bottom
        val glyphPanel = GlyphPanel(Positions.offset1x1())
        val palettePanel = PalettePanel(Positions.relativeToBottomOf(glyphPanel.getPanel()))
        val toolsPanel = ToolsPanel(Positions.relativeToBottomOf(palettePanel.getPanel()))
        val layersPanel = LayersPanel(Positions.relativeToBottomOf(toolsPanel.getPanel()))
        val drawPanel = DrawPanel(
                Positions.relativeToRightOf(glyphPanel.getPanel()),
                Sizes.create(tileGrid.size().width().minus(20), tileGrid.size().height().minus(2))
        )

        // Create controllers and wire them up to panels
        val glyphController = GlyphController(glyphPanel)
        glyphPanel.onMouseAction(glyphController)

        val paletteController = PaletteController(palettePanel)
        palettePanel.onMouseAction(paletteController)

        val drawController = DrawController(drawPanel, glyphPanel, palettePanel)
        drawPanel.onMouseAction(drawController)

        // Add panels to screen
        screen.addComponent(glyphPanel.getPanel())
        screen.addComponent(palettePanel.getPanel())
        screen.addComponent(toolsPanel.getPanel())
        screen.addComponent(layersPanel.getPanel())
        screen.addComponent(drawPanel.getPanel())
    }

    override fun display() {
        screen.display()
    }

    override fun respondToUserInput(input: Input): View {
        return this
    }
}
