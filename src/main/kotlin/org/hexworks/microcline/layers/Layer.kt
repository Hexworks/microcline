package org.hexworks.microcline.layers

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.microcline.config.Config
import org.hexworks.zircon.api.Layers


class Layer(initialLabel: String,
            initialLocked: Boolean = false,
            initialVisible: Boolean = true,
            initialSelected: Boolean = false) {

    val labelProperty = createPropertyFrom(initialLabel)
    val lockedProperty = createPropertyFrom(initialLocked)
    val visibleProperty = createPropertyFrom(initialVisible)
    val selectedProperty = createPropertyFrom(initialSelected)

    val layer = Layers.newBuilder()
            .withSize(Config.DRAW_SIZE)
            .build()

}
