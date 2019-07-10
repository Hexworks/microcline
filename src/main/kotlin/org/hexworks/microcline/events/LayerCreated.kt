package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.microcline.data.DrawLayer

data class LayerCreated(val layer: DrawLayer) : Event
