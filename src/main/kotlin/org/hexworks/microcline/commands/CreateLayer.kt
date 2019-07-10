package org.hexworks.microcline.commands

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot

class CreateLayer : Command {

    override val name = "Create Layer"

    override fun toString() = name

    override fun execute(state: DrawingSnapshot): DrawingSnapshot {
        return state.copy(
                layers = state.layers.plus(DrawLayer(
                        name = "Layer ${state.layers.size}")))
    }
}
