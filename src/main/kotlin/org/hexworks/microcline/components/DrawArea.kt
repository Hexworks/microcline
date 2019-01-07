package org.hexworks.microcline.components

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.controllers.DrawAreaController
import org.hexworks.microcline.controllers.DrawController
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer


class DrawArea(position: Position,
               val state: State,
               val wrapper: Panel = Components.panel()
                       .wrapWithBox(true)
                       .withSize(Size.create(Config.WINDOW_WIDTH, Config.DRAW_AREA_HEIGHT + 2 * Config.BORDER_SIZE))
                       .withPosition(position)
//                       .withComponentRenderer(NoOpComponentRenderer())
                       .build().apply {
                           // This borderless inner panel is the actual draw surface.
                           // It is needed so that we don't have to worry about drawing onto the borders.

//                           addComponent(state.canvas)

//                           addComponent(Components.panel()
//                                   .withSize(Size.create(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT))
//                                   .withPosition(Position.zero())
//                                   .withComponentRenderer(NoOpComponentRenderer())
//                                   .build().apply {
//                                       // Attach controller to panel.
//                                       onMouseAction(DrawAreaController(state, position))
//                                       state.canvas = this
//                                   }
//                           )

                       }

) {

    init {
        rebuild()


        println("${wrapper.children}")

        // Subscribe to layers changed --> rebuild()
    }

    private fun rebuild() {
        wrapper.detachAllComponents()

        // First the top component is added where we attach the MouseListener to.
        wrapper.addComponent(Components.panel()
                .withSize(Config.DRAW_AREA_WIDTH, Config.DRAW_AREA_HEIGHT)
                .withPosition(Position.zero())
                .withComponentRenderer(NoOpComponentRenderer())
                .build().apply {
//                    onMouseAction(DrawAreaController(state, this.position))
                    onMouseAction(DrawController(state, this))

//                    fill(state.tile.withForegroundColor(ANSITileColor.RED))
                    draw(state.tile.withBackgroundColor(ANSITileColor.YELLOW), Position.create(20, 20))
                }
        )

        // Then add each canvas. (in what order?)
        state.canvasList.forEachIndexed { i, id ->
            val p = state.canvasMap[id]
            if (p != null) {
                p.draw(state.tile.withForegroundColor(ANSITileColor.values()[i+1]), Position.create(i+1, i+1))
                wrapper.addComponent(p)
            }
        }

    }

}
