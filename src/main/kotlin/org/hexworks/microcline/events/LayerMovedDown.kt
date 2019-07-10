package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event

data class LayerMovedDown(val newIndex: Int) : Event
