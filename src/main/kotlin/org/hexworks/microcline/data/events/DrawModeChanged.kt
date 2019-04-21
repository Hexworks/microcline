package org.hexworks.microcline.data.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.microcline.drawtools.DrawTool

data class DrawModeChanged(val mode: DrawTool) : Event
