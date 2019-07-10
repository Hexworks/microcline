package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.microcline.extensions.replaceAt

class SetLayerVisibility(private val layerIndex: Int,
                         private val isVisible: Boolean) : Command {

    override val name = "Set Layer Visibility"

    override fun toString() = "$name at $layerIndex to $isVisible."

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return if (layerIndex in 0..state.layers.lastIndex) {
            val layers = state.layers
            val layer = layers[layerIndex]
            if (layer.isVisible != isVisible) {
                val newLayer = layer.copy(isVisible = isVisible)
                state.copy(layers = state.layers.replaceAt(layerIndex, newLayer))
            } else state
        } else state
    }
}
