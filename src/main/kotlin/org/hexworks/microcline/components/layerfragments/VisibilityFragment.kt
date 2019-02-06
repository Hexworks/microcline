package org.hexworks.microcline.components.layerfragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.microcline.data.events.LayerOrderChanged
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.kotlin.onSelectionChanged
import org.hexworks.zircon.internal.Zircon


class VisibilityFragment(position: Position, visibilityProperty: Property<Boolean>) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("V")
            .wrapSides(false)
            .build().apply {
                // TODO: .withIsSelected() does not work
                isSelected = visibilityProperty.value

                onSelectionChanged {
                    visibilityProperty.value = it.newValue
                    Zircon.eventBus.publish(LayerOrderChanged(true))
                }
            }

    init {
        root.isSelected = visibilityProperty.value
        visibilityProperty.onChange {
            root.isSelected = it.newValue
        }
    }

}
