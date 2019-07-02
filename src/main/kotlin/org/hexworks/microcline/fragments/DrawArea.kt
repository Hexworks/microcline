package org.hexworks.microcline.fragments

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.extensions.box


class DrawArea(context: EditorContext) : Fragment {

    override val root = Components.panel()
            .withDecorations(box())
            .withSize(Config.DRAW_AREA_SIZE)
            .build().apply {
                addComponent(context.drawPanel)
            }

}
