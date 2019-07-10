package org.hexworks.microcline.data.impl

import org.hexworks.microcline.commands.Command
import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile

/**
 * This implementation of [Drawing] uses *Event Sourcing* to enable
 * [undo] and [redo] operations. It will *compactify* state when the
 * history gets greater than [historySize]. This means that [undo]
 * is only possible for the last [historySize] steps.
 */
class EventSourcedCompactifyingDrawing(
        private val historySize: Int,
        private var baseState: DrawingSnapshot) : Drawing {

    private val currentCommands = mutableListOf<Command>()
    private val redoCommands = mutableListOf<Command>()

    private var currentState = baseState

    override val state: DrawingSnapshot
        get() = currentState

    private val canUndo: Boolean
        get() = currentCommands.isNotEmpty()

    private val canRedo: Boolean
        get() = redoCommands.isNotEmpty()

    override fun execute(command: Command): Map<Position, Tile> {
        redoCommands.clear()
        currentCommands.add(command)
        val prevState = currentState
        if (currentCommands.size > historySize) {
            baseState = currentCommands.removeAt(0).execute(baseState)
            refresh()
        } else {
            currentState = command.execute(currentState)
        }
        return currentState.diff(prevState)
    }

    override fun undo() {
        if (canUndo) {
            redoCommands.add(0, currentCommands.removeAt(currentCommands.lastIndex))
            refresh()
        }
    }

    override fun redo() {
        if (canRedo) {
            currentCommands.add(redoCommands.removeAt(0))
            refresh()
        }
    }

    private fun refresh() {
        currentState = currentCommands.toList().fold(baseState) { state, cmd ->
            cmd.execute(state)
        }
    }
}
