package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot

class MoveLayerUp(private val layerIndex: Int) : Command {

    override val name = "Move Layer Up"

    override fun toString() = "$name at $layerIndex"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return if (layerIndex in 0..state.layers.lastIndex && layerIndex > 0) {
            val result = state.layers.toMutableList().apply {
                add(layerIndex - 1, removeAt(layerIndex))
            }
            state.copy(layers = result)
        } else state
    }
}
