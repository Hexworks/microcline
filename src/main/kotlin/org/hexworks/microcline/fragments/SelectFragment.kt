package org.hexworks.microcline.fragments

import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.layers.Layer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onComponentEvent
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.Processed

class SelectFragment(position: Position,
                     layer: Layer,
                     private val context: EditorContext) : Fragment {

    override val root = Components.toggleButton()
            .withPosition(position)
            .withText("Select")
            .wrapSides(false)
            .build().apply {
                onComponentEvent(ACTIVATED) {
                    // Should not really toggle, but should work like a radio button.
                    this.isSelected = true
                    // TODO: move this to a service
                    context.layerRegistry.select(layer)
                    Processed
                }
                selectedProperty.bind(layer.selectedProperty)
            }

}
