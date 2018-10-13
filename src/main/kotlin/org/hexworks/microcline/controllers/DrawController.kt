package org.hexworks.microcline.controllers

import org.hexworks.microcline.common.DrawMode
import org.hexworks.microcline.panels.DrawPanel
import org.hexworks.microcline.panels.GlyphPanel
import org.hexworks.microcline.panels.PalettePanel
import org.hexworks.microcline.panels.ToolsPanel
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.builder.graphics.LayerBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener
import org.hexworks.zircon.api.shape.LineFactory


class DrawController(
        private val grid: TileGrid,
        private val drawPanel: DrawPanel,
        private val glyphPanel: GlyphPanel,
        private val palettePanel: PalettePanel,
        private val toolsPanel: ToolsPanel
) : MouseListener {

    private var startPosition: Position = TOP_LEFT_CORNER
    private lateinit var tempLayer: Layer

    override fun mousePressed(action: MouseAction) {
        when (toolsPanel.selectedMode()) {
            DrawMode.FREE -> drawTile(action)
            else -> {
                startPosition = action.position
                tempLayer = LayerBuilder.newBuilder()
                        .withSize(drawPanel.size)
                        .withOffset(drawPanel.position)
                        .build()
                grid.pushLayer(tempLayer)
            }
        }
    }

    override fun mouseReleased(action: MouseAction) {
        when (toolsPanel.selectedMode()) {
            DrawMode.FREE -> {}
            else -> {
                println("save layer")
//                tempLayer.fetchPositions().map {
//                    val t = tempLayer.getAbsoluteTileAt(it)
//                    if (t.isPresent) {
//                        grid.setTileAt(it, t.get())
//                    }
//                }
//                grid.removeLayer(tempLayer)
                startPosition = TOP_LEFT_CORNER
            }
        }
    }

    override fun mouseDragged(action: MouseAction) {
        when (toolsPanel.selectedMode()) {
            DrawMode.FREE -> drawTile(action)
            DrawMode.LINE -> {
                // TODO: Zircon Layer should have a clear() method.
                tempLayer.fetchPositions().map {
                    tempLayer.setAbsoluteTileAt(it, Tile.empty())
                }
                drawLine(action, tempLayer)
            }
        }
    }

    private fun drawTile(action: MouseAction) {
        drawPanel.draw(
                drawable = Tiles.newBuilder()
                        .withCharacter(glyphPanel.selectedGlyph().character)
                        .withBackgroundColor(palettePanel.selectedBackgroundColor())
                        .withForegroundColor(palettePanel.selectedForegroundColor()).build(),
                position = action.position.minus(drawPanel.position))
    }

    private fun drawLine(action: MouseAction, surface: DrawSurface) {
        val lineStart = startPosition.minus(startPosition)
        val lineEnd = action.position.minus(startPosition)
        val line = LineFactory.buildLine(lineStart, lineEnd)
        val tile = Tiles.newBuilder()
                .withCharacter(glyphPanel.selectedGlyph().character)
                .withBackgroundColor(palettePanel.selectedBackgroundColor())
                .withForegroundColor(palettePanel.selectedForegroundColor()).build()
        line.forEach {
            surface.draw(tile, startPosition.minus(drawPanel.position).plus(it))
        }
    }

    companion object {
        private val TOP_LEFT_CORNER = Positions.create(2, 2)
    }
}
