package org.hexworks.microcline.commands

import org.hexworks.microcline.data.Drawing
import org.hexworks.microcline.data.DrawingSnapshot

/**
 * Represents a command which can be executed
 * on a [Drawing].
 */
interface Command {

    val name: String

    /**
     * Executes this [Command] and produces a new [DrawingSnapshot]
     * from the given [state].
     */
    fun execute(state: DrawingSnapshot): DrawingSnapshot
}
