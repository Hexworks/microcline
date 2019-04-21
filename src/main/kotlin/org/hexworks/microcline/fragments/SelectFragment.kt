package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position

class SelectFragment(position: Position,
                     layer: DrawLayer) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("Selected")
            .wrapSides(false)
            .build().apply {
                selectedProperty.bindBidirectional(layer.selectedProperty)
            }

}
