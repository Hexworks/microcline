package org.hexworks.microcline.views

import org.hexworks.microcline.controllers.DrawController
import org.hexworks.microcline.controllers.GlyphController
import org.hexworks.microcline.controllers.PaletteController
import org.hexworks.microcline.components.*
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.grid.TileGrid


class DrawView(tileGrid: TileGrid) : BaseView(tileGrid) {

    init {
        // Create the components, top to bottom
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

        // Create controllers and wire them up to components
        val glyphController = GlyphController(glyphPanel)
        glyphPanel.onMouseAction(glyphController)

        val paletteController = PaletteController(palettePanel)
        palettePanel.onMouseAction(paletteController)

        val drawController = DrawController(tileGrid, drawPanel, glyphPanel, palettePanel, toolsPanel)
        drawPanel.onMouseAction(drawController)

        // Add components to screen
        screen.addComponent(glyphPanel.getPanel())
        screen.addComponent(palettePanel.getPanel())
        screen.addComponent(toolsPanel.getPanel())
        screen.addComponent(layersPanel.getPanel())
        screen.addComponent(drawPanel.getPanel())
    }

}
