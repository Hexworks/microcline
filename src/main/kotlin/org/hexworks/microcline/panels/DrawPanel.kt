package org.hexworks.microcline.panels

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.util.Consumer

class DrawPanel(
        position: Position,
        size: Size,
        glyphPanel: GlyphPanel,
        palettePanel: PalettePanel,
        private val panel: Panel = Components.panel()
                .wrapWithBox()
                .title("Draw")
                .size(size)
                .position(position)
                .build()
): Panel by panel {

    private var active = false

    init {
        this.onMousePressed(object : Consumer<MouseAction> {
            override fun accept(p: MouseAction) {
                this@DrawPanel.draw(
                        drawable = Tiles.newBuilder()
                                .character(glyphPanel.getGlyph().character)
                                .backgroundColor(palettePanel.getBackgroundColor())
                                .foregroundColor(palettePanel.getForegroundColor()).build(),
                        position = p.position.minus(this@DrawPanel.position()))
                this@DrawPanel.active = true
            }
        })

        this.onMouseReleased(object : Consumer<MouseAction> {
            override fun accept(p: MouseAction) {
                this@DrawPanel.active = false
            }
        })

        panel.onMouseMoved(object : Consumer<MouseAction> {
            override fun accept(p: MouseAction) {
                println("POS: ${p.position}, active: ${this@DrawPanel.active}")
                if (this@DrawPanel.active) {
                    this@DrawPanel.draw(
                            drawable = Tiles.newBuilder()
                                    .character(glyphPanel.getGlyph().character)
                                    .backgroundColor(palettePanel.getBackgroundColor())
                                    .foregroundColor(palettePanel.getForegroundColor()).build(),
                            position = p.position.minus(this@DrawPanel.position()))
                }
            }
        })

    }

    fun getPanel() = panel

}
