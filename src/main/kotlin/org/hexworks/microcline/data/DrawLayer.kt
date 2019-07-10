package org.hexworks.microcline.data

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile

/**
 * A [DrawLayer] is a collection of [Tile]s at
 * given [Position]s. By default it is *not selected*,
 * *visible* and *not locked*.
 */
data class DrawLayer(
        val name: String,
        val isSelected: Boolean = false,
        val isVisible: Boolean = true,
        val isLocked: Boolean = false,
        val tiles: Map<Position, Tile> = mapOf())
