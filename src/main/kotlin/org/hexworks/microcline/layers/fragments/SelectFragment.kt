package org.hexworks.microcline.layers.fragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.microcline.layers.Layer
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.kotlin.onMouseReleased


class SelectFragment(position: Position, layer: Layer) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("S")
            .wrapSides(false)
            .build().apply {
                onMouseReleased {
                    // Should not really toggle, but should work like a radio button.
                    this.isSelected = true
                    State.layerRegistry.select(layer)
                }
            }

    init {
        root.isSelected = layer.selectedProperty.value
        layer.selectedProperty.onChange {
            root.isSelected = it.newValue
        }
    }

}
