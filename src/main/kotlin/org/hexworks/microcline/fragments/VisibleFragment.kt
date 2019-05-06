package org.hexworks.microcline.fragments

import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.services.DrawLayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position


class VisibleFragment(position: Position,
                      layer: DrawLayer,
                      drawLayerEditor: DrawLayerEditor) : Fragment {

    override val root = Components.checkBox()
            .withPosition(position)
            .build().apply {
                selectedProperty.bind(layer.visibleProperty)
                selectedProperty.onChange {
                    drawLayerEditor.redrawLayers()
                }
            }
}
