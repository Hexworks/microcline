package org.hexworks.microcline.components.layerfragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.layers.Layer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Pass
import org.hexworks.zircon.api.uievent.Processed


class RemoveFragment(position: Position,
                     layer: Layer,
                     private val context: EditorContext) : Fragment {

    override val root = Components.button()
            .withPosition(position)
            .withText("X")
            .wrapSides(false)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    if (isEnabled) {
                        // TODO: use service
                        context.layerRegistry.remove(layer)
                        Processed
                    } else Pass
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
