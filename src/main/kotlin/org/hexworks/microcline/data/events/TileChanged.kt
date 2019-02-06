package org.hexworks.microcline.data.events

import org.hexworks.cobalt.events.api.Event
import org.hexworks.zircon.api.data.Tile


data class TileChanged(val tile: Tile) : Event
