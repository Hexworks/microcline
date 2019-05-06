package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.expression.not
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.services.DrawLayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed

/**
 * [Fragment] which adds the **Clear** operation for a [Layer].
 */
class ClearFragment(position: Position,
                    layer: DrawLayer,
                    drawLayerEditor: DrawLayerEditor)
    : Fragment {

    override val root = Components.button()
            .withPosition(position)
            .withText("Clear")
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    layer.clear()
                    drawLayerEditor.redrawLayers()
                    Processed
                }
                enabledProperty.updateFrom(layer.lockedProperty.not())
            }
}
