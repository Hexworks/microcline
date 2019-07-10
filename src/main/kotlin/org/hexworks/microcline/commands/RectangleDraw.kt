package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.shape.LineFactory

class RectangleDraw(
        private val startPosition: Position,
        private val endPosition: Position) : Command {

    override val name = "Draw Rectangle"

    override fun toString() = "$name from $startPosition to $endPosition"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        val selectedTile = state.selectedTile
        val selectedLayer = state.selectedLayer
        val oldTiles = selectedLayer.tiles
        val newTiles = listOf(
                LineFactory.buildLine(
                        startPosition,
                        Positions.create(endPosition.x, startPosition.y)),
                LineFactory.buildLine(
                        startPosition,
                        Positions.create(startPosition.x, endPosition.y)),
                LineFactory.buildLine(
                        endPosition,
                        Positions.create(endPosition.x, startPosition.y)),
                LineFactory.buildLine(
                        endPosition,
                        Positions.create(startPosition.x, endPosition.y)))
                .flatMap {
                    it.positions()
                }.map { pos ->
                    pos to selectedTile
                }
        return state.withSelectedLayer(selectedLayer.copy(tiles = oldTiles.plus(newTiles)))
    }
}
