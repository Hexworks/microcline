package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position


class VisibilityFragment(position: Position,
                         layer: DrawLayer) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("Visible")
            .wrapSides(false)
            .build().apply {
                selectedProperty.bind(layer.visibleProperty)
            }
}
