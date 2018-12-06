package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event


data class MousePosition(val x: Int, val y: Int) : Event
