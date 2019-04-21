package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position


class LabelFragment(position: Position, width: Int, labelProperty: Property<String>) : Fragment {

    override val root = Components.textArea()
            .withPosition(position)
            .withSize(width, 1)
            .withText(labelProperty.value)
            .build().apply {
                disable()
            }

}
