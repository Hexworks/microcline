package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.microcline.extensions.replaceAt

class SetLayerLockState(private val layerIndex: Int,
                        private val isLocked: Boolean) : Command {

    override val name = "Set Layer Lock State"

    override fun toString() = "$name at $layerIndex to $isLocked."

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return if (layerIndex in 0..state.layers.lastIndex) {
            val layers = state.layers
            val layer = layers[layerIndex]
            if (layer.isLocked != isLocked) {
                val newLayer = layer.copy(isLocked = isLocked)
                state.copy(layers = state.layers.replaceAt(layerIndex, newLayer))
            } else state
        } else state
    }
}
