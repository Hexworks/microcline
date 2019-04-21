package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onSelectionChanged


class LockFragment(position: Position, lockedProperty: Property<Boolean>) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("L")
            .wrapSides(false)
            .build().apply {
                // TODO: .withIsSelected() does not work
                isSelected = lockedProperty.value

                onSelectionChanged {
                    lockedProperty.value = it.newValue
                }
            }

}
