package org.hexworks.microcline.components

import org.hexworks.microcline.config.NewConfig
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
                       .withSize(Size.create(NewConfig.WINDOW_WIDTH, NewConfig.DRAW_AREA_HEIGHT + 2 * NewConfig.BORDER_SIZE))
                       .withPosition(position)
                       .withComponentRenderer(NoOpComponentRenderer())
                       .build(),
               // This borderless inner panel is the actual draw area.
               // It is needed so that we don't have to worry about drawing onto the border of the wrapper.
               val panel: Panel = Components.panel()
                       .withSize(Size.create(NewConfig.DRAW_AREA_WIDTH, NewConfig.DRAW_AREA_HEIGHT))
                       .withPosition(Position.zero())
                       .withComponentRenderer(NoOpComponentRenderer())
                       .build()
) {

    init {
        // The panel must be added to the wrapper.
        wrapper.addComponent(panel)

        // Attach controller to panel.
        panel.onMouseAction(DrawAreaController(state, position))
    }

}
