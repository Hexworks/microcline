package org.hexworks.microcline.drawings

import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.listener.MouseListener


interface Drawing {

    val startPosition: Position

    val panel: Panel

    val controller: MouseListener

}
