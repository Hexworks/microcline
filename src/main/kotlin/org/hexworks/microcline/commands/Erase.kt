package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.data.Position

class Erase(
        private val touchedPositions: Iterable<Position>) : Command {

    override val name = "Erase"

    override fun toString() = "$name positions ${touchedPositions.joinToString()}"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        val selectedLayer = state.selectedLayer
        val oldTiles = selectedLayer.tiles
        val tilesToRemove = touchedPositions.toSet()
        val newTiles = oldTiles.filterKeys {
            tilesToRemove.contains(it).not()
        }

        return state.withSelectedLayer(selectedLayer.copy(tiles = newTiles))
    }
}
