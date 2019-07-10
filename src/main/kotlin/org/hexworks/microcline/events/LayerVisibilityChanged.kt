package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event

data class LayerVisibilityChanged(
        val index: Int,
        val visible: Boolean) : Event
