package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.data.Position

class FreeHandDraw(
        private val touchedPositions: Iterable<Position>) : Command {

    override val name = "Free Hand Draw"

    override fun toString() = "$name at positions ${touchedPositions.joinToString()}"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        val selectedTile = state.selectedTile
        val selectedLayer = state.selectedLayer
        val oldTiles = selectedLayer.tiles
        val newTiles = touchedPositions.map { it to selectedTile }.toMap()

        return state.withSelectedLayer(selectedLayer.copy(tiles = oldTiles.plus(newTiles)))
    }
}
