package org.hexworks.microcline.layers.fragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.kotlin.onMouseReleased


class ClearFragment(position: Position, lockedProperty: Property<Boolean>, layer: Layer) : Fragment {

    override val root = Components.button()
            .withPosition(position)
            .withText("C")
            .wrapSides(false)
            .build().apply {
                onMouseReleased {
                    if (isEnabled) {
                        layer.clear()
                    }
                }
            }

    init {
        // If layer is locked then clear is not allowed.
        if (lockedProperty.value) {
            root.disable()
        }
        lockedProperty.onChange {
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
