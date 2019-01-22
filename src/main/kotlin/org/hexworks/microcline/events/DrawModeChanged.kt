package org.hexworks.microcline.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.microcline.drawers.Drawer


data class DrawModeChanged(val mode: Drawer) : Event
