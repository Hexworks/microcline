package org.hexworks.microcline.fragments

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.extensions.processMouseEvents
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.component.renderer.ComponentRenderContext
import org.hexworks.zircon.api.component.renderer.ComponentRenderer
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.graphics.impl.SubTileGraphics
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.UIEventPhase


class DrawArea(drawSurface: Component, mousePositionHandler: (Position) -> Unit) : Fragment {

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

    init {
        drawSurface.processMouseEvents(MouseEventType.MOUSE_MOVED, UIEventPhase.TARGET) {
            mousePositionHandler(it.position.minus(offset))
        }
        drawSurface.processMouseEvents(MouseEventType.MOUSE_DRAGGED, UIEventPhase.TARGET) {
            mousePositionHandler(it.position.minus(offset))
        }
    }

    companion object {
        private val offset = Positions.create(1, 4)
    }

}
