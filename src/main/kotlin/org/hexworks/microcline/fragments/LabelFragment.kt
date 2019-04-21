package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position

class LabelFragment(position: Position,
                    width: Int,
                    layer: DrawLayer) : Fragment {

    override val root = Components.label()
            .withPosition(position)
            .withSize(width, 1)
            .withText(layer.labelProperty.value)
            .build()

}
