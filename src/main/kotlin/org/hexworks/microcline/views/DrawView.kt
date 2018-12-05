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
        val palettePanel = PalettePanel(Positions.bottomLeftOf(glyphPanel.getPanel()))
        val toolsPanel = ToolsPanel(Positions.bottomLeftOf(palettePanel.getPanel()))
        val layersPanel = LayersPanel(Positions.bottomLeftOf(toolsPanel.getPanel()))
        val drawPanel = DrawPanel(
                Positions.topRightOf(glyphPanel.getPanel()),
                Sizes.create(tileGrid.size.width.minus(20), tileGrid.size.height.minus(2))
        )

        // TODO: controllers and views should be instantiated in main (or some external entity)
        // TODO: I'm not sure how this should work but we should think about solutions
        // Create controllers and wire them up to panels
        val glyphController = GlyphController(glyphPanel)
        glyphPanel.onMouseAction(glyphController)

        val paletteController = PaletteController(palettePanel)
        palettePanel.onMouseAction(paletteController)

        // TODO: having DrawController implement mouse listener is a nice touch!
        val drawController = DrawController(tileGrid, drawPanel, glyphPanel, palettePanel, toolsPanel)
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
