package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.expression.not
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position

class SelectFragment(position: Position,
                     layer: DrawLayer) : Fragment {

    override val root = Components.checkBox()
            .withPosition(position)
            .build().apply {
                selectedProperty.bindBidirectional(layer.selectedProperty)
                enabledProperty.bind(layer.selectedProperty.not())
            }

}
