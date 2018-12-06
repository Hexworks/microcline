package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.microcline.common.DrawMode


data class DrawModeChanged(val mode: DrawMode) : Event
