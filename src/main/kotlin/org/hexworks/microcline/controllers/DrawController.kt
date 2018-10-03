package org.hexworks.microcline.controllers

import org.hexworks.microcline.panels.DrawPanel
import org.hexworks.microcline.panels.GlyphPanel
import org.hexworks.microcline.panels.PalettePanel
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener


class DrawController(
        private val drawPanel: DrawPanel,
        private val glyphPanel: GlyphPanel,
        private val palettePanel: PalettePanel
) : MouseListener {

    override fun mousePressed(action: MouseAction) {
        drawPanel.draw(
                drawable = Tiles.newBuilder()
                        .character(glyphPanel.selectedGlyph().character)
                        .backgroundColor(palettePanel.selectedBackgroundColor())
                        .foregroundColor(palettePanel.selectedForegroundColor()).build(),
                position = action.position.minus(drawPanel.position()))
    }

    override fun mouseDragged(action: MouseAction) {
        mousePressed(action)
    }

}
