package org.hexworks.microcline.data.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.microcline.layers.Layer

data class LayerMovedUp(val layer: Layer): Event
