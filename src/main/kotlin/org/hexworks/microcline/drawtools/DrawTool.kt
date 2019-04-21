package org.hexworks.microcline.drawtools

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.graphics.DrawSurface

/**
 * Represents a tool which can be used for drawing on a
 * [DrawSurface].
 */
interface DrawTool {

    val name: String

    /**
     * Executes the given [DrawCommand] on the given [surface].
     */
    fun draw(command: DrawCommand, surface: DrawSurface)

}
