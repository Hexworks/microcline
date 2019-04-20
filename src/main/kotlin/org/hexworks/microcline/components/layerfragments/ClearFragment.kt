package org.hexworks.microcline.components.layerfragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed


class ClearFragment(position: Position, lockedProperty: Property<Boolean>, layer: Layer) : Fragment {

    override val root = Components.button()
            .withPosition(position)
            .withText("C")
            .wrapSides(false)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    if (isEnabled) {
                        layer.clear()
                        Processed
                    } else Pass
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
