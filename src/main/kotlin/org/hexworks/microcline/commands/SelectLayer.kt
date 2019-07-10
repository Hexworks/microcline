package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawingSnapshot

class SelectLayer(private val layerIndex: Int) : Command {

    override val name = "Select Layer"

    override fun toString() = "$name at $layerIndex"

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return if (layerIndex in 0..state.layers.lastIndex) {
            val layers = state.layers.toMutableList()
            val prevSelected = state.selectedLayer
            val newSelected = state.layers[layerIndex]
            layers[state.selectedLayerIdx] = prevSelected.copy(isSelected = false)
            layers[layerIndex] = newSelected.copy(isSelected = true)
            state.copy(
                    selectedLayerIdx = layerIndex,
                    layers = layers.toList())
        } else state
    }
}
