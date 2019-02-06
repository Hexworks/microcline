package org.hexworks.microcline.components.layerfragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.microcline.layers.Layer
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.kotlin.onMouseReleased


class RemoveFragment(position: Position, layer: Layer) : Fragment {

    override val root = Components.button()
            .withPosition(position)
            .withText("X")
            .wrapSides(false)
            .build().apply {
                onMouseReleased {
                    if (isEnabled) {
                        State.layerRegistry.remove(layer)
                    }
                }
            }

    init {
        // If layer is locked then clear is not allowed.
        if (layer.lockedProperty.value) {
            root.disable()
        }
        layer.lockedProperty.onChange {
            when (it.newValue) {
                true -> {
                    root.disable()
                }
                false -> {
                    root.enable()
                }
            }
        }
    }

}
