package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.extensions.onChange
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.services.DrawLayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType


class VisibleFragment(position: Position,
                      layer: DrawLayer,
                      drawLayerEditor: DrawLayerEditor) : Fragment {

    override val root = Components.checkBox()
            .withPosition(position)
            .build().apply {
                selectedProperty.bindBidirectional(layer.visibleProperty)
                selectedProperty.onChange {
                    drawLayerEditor.redrawLayers()
                }
            }
}
