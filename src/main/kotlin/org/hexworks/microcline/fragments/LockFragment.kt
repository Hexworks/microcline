package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position


class LockFragment(position: Position,
                   layer: DrawLayer) : Fragment {

    override val root = Components.checkBox()
            .withPosition(position)
            .build().apply {
                selectedProperty.bind(layer.lockedProperty)
            }

}
