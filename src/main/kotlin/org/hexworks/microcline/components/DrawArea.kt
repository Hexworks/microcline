package org.hexworks.microcline.components

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.controllers.DrawController
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile


class DrawArea(position: Position,
               val panel: Panel = Components.panel()
                          .wrapWithBox(true)
                          .withSize(Size.create(Config.WINDOW_WIDTH, Config.DRAW_AREA_HEIGHT + 2 * Config.BORDER_SIZE))
                          .withPosition(position)
                          .build().apply {
                              val gc = Components.gameComponent<Tile, Block<Tile>>()
                                      .withSize(contentSize)
                                      .withVisibleSize(Sizes.from2DTo3D(contentSize))
                                      .withGameArea(State.drawing)
                                      .build().apply {
                                          onMouseAction(DrawController())
                                      }
                              addComponent(gc)
                          })
