package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.microcline.drawings.Drawing


data class DrawingFinished(val drawing: Drawing) : Event
