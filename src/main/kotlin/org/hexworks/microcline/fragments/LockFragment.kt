package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onSelectionChanged


class LockFragment(position: Position,
                   layer: DrawLayer) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("L")
            .wrapSides(false)
            .build().apply {
                isSelected = layer.lockedProperty.value

                onSelectionChanged {
                    layer.lockedProperty.value = it.newValue
                }
            }

}
