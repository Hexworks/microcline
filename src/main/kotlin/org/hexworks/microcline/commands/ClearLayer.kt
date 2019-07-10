package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.microcline.extensions.replaceAt

class ClearLayer(private val layerIndex: Int) : Command {

    override val name = "Clear Layer"

    override fun toString() = "$name at $layerIndex."

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return if (layerIndex in 0..state.layers.lastIndex) {
            val layers = state.layers
            val layer = layers[layerIndex]
            if (layer.tiles.isEmpty()) {
                state
            } else state.copy(layers = layers.replaceAt(layerIndex, layer.copy(tiles = mapOf())))
        } else state
    }
}
