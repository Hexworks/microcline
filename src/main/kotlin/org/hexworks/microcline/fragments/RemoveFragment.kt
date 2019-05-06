package org.hexworks.microcline.fragments

import org.hexworks.cobalt.databinding.api.expression.and
import org.hexworks.cobalt.databinding.api.expression.not
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.events.LayerChanged
import org.hexworks.microcline.services.DrawLayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.Zircon


class RemoveFragment(position: Position,
                     layer: DrawLayer,
                     private val drawLayerEditor: DrawLayerEditor) : Fragment {

    override val root = Components.button()
            .withPosition(position)
            .withText("Remove")
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    drawLayerEditor.removeLayer(layer)
                    Zircon.eventBus.publish(LayerChanged)
                    Processed
                }
                enabledProperty.updateFrom(layer.lockedProperty.not().and(layer.selectedProperty.not()))
            }
}
