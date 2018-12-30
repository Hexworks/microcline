package org.hexworks.microcline.components

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.controllers.DrawAreaController
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class DrawArea(position: Position,
               state: State,
               val wrapper: Panel = Components.panel()
                       .wrapWithBox(true)
                       .withSize(Size.create(Config.WINDOW_WIDTH, Config.DRAW_AREA_HEIGHT + 2 * Config.BORDER_SIZE))
                       .withPosition(position)
                       .withComponentRenderer(NoOpComponentRenderer())
                       .build().apply {
                           // This borderless inner panel is the actual draw area.
                           // It is needed so that we don't have to worry about drawing onto the border of the wrapper.
                           addComponent(Components.panel()
                                   .withSize(Size.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT))
                                   .withPosition(Position.zero())
                                   .withComponentRenderer(NoOpComponentRenderer())
                                   .build().apply {
                                       // Attach controller to panel.
                                       onMouseAction(DrawAreaController(state, position))
                                   }
                           )
                       }
)
