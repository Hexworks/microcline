package org.hexworks.microcline.data

import org.hexworks.microcline.commands.Command
import org.hexworks.microcline.data.impl.EventSourcedCompactifyingDrawing
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.Tiles

/**
 * Represents a *layered* image composed of *tile*s which is
 * being edited by the user.
 */
interface Drawing {

    /**
     * The current state of the [Drawing]
     */
    val state: DrawingSnapshot

    /**
     * Executes the [Command] on this [Drawing] and returns the difference
     * compared to the previous state. Will use [Tiles.empty] for removed
     * [Tile]s.
     */
    fun execute(command: Command): Map<Position, Tile>

    /**
     * Undoes the last [Command] (if any).
     */
    fun undo()

    /**
     * Redoes the last [Command] (if any).
     */
    fun redo()

    companion object {

        /**
         * Creates a new [Drawing] using the given [DrawingSnapshot].
         */
        fun create(baseState: DrawingSnapshot,
                   historySize: Int): Drawing {

            return EventSourcedCompactifyingDrawing(
                    historySize = historySize,
                    baseState = baseState)
        }

        /**
         * Creates a new empty [Drawing] with defaults.
         */
        fun create(historySize: Int = 100,
                   tile: Tile): Drawing {

            return EventSourcedCompactifyingDrawing(
                    historySize = historySize,
                    baseState = DrawingSnapshot(tile))
        }
    }
}
