package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.shape.LineFactory

class LineDraw(private val startPosition: Position,
               private val endPosition: Position) : Command {

    override val name = "Draw Line"

    override fun toString() = "$name from $startPosition to $endPosition"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        val selectedTile = state.selectedTile
        val selectedLayer = state.selectedLayer
        val oldTiles = selectedLayer.tiles
        val newTiles = LineFactory.buildLine(startPosition, endPosition)
                .map { it to selectedTile }

        return state.withSelectedLayer(selectedLayer.copy(tiles = oldTiles.plus(newTiles)))
    }
}
