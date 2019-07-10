package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot

class DeleteLayer(private val layerIndex: Int) : Command {

    override val name = "Delete Layer"

    override fun toString() = "$name at $layerIndex."

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return if (layerIndex in 0..state.layers.lastIndex && state.selectedLayerIdx != layerIndex) {
            val layers = state.layers
            val selectedLayerIdx = state.selectedLayerIdx
            val newSelectedLayerIdx = if (layerIndex < selectedLayerIdx) {
                selectedLayerIdx - 1
            } else selectedLayerIdx
            state.copy(
                    layers = layers.toMutableList().apply {
                        removeAt(layerIndex)
                    },
                    selectedLayerIdx = newSelectedLayerIdx)
        } else state
    }
}
