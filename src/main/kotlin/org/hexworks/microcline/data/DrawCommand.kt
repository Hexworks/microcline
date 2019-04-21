package org.hexworks.microcline.data

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile

data class DrawCommand(val tile: Tile,
                       val startPosition: Position,
                       val endPosition: Position,
                       val finished: Boolean)
