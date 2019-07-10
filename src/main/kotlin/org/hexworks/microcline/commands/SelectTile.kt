package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.data.Tile

class SelectTile(private val tile: Tile) : Command {

    override val name = "Select Layer"

    override fun toString() = "$name: $tile"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return state.copy(selectedTile = tile)
    }
}
