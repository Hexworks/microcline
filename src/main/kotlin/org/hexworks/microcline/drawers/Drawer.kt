package org.hexworks.microcline.drawers

import org.hexworks.microcline.data.DrawCommand
import org.hexworks.zircon.api.graphics.DrawSurface


interface Drawer {

    fun name(): String

    fun draw(command: DrawCommand, surface: DrawSurface)

}
