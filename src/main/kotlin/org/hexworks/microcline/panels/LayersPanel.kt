package org.hexworks.microcline.panels

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position

const val LAYERS_P_SIZE_X = 16
const val LAYERS_P_SIZE_Y = 13


class LayersPanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .title("Layers")
                .size(Sizes.create(LAYERS_P_SIZE_X, LAYERS_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .position(position)
                .build()
): Panel by panel {

    fun getPanel() = panel

}
