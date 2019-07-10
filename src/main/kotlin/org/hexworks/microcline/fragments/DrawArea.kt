package org.hexworks.microcline.fragments

import org.hexworks.microcline.config.Config
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.component.renderer.ComponentRenderContext
import org.hexworks.zircon.api.component.renderer.ComponentRenderer
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.graphics.impl.SubTileGraphics


class DrawArea(drawSurface: Component) : Fragment {

    override val root = Components.panel()
            .withDecorations(box())
            .withComponentRenderer(object : ComponentRenderer<Panel>{
                override fun render(tileGraphics: SubTileGraphics, context: ComponentRenderContext<Panel>) {
                    tileGraphics.fill(Config.DRAW_AREA_BACKGROUND)
                }
            })
            .withSize(Config.DRAW_AREA_SIZE)
            .build().apply {
                addComponent(drawSurface)
            }

}
