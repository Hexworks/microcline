package org.hexworks.microcline.fragments

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.game.GameComponent


class DrawArea(position: Position,
               private val context: EditorContext) : Fragment {

    val gameComponent: GameComponent<CharacterTile, Block<CharacterTile>>

    override val root = Components.panel()
            .wrapWithBox(true)
            .withSize(Size.create(Config.WINDOW_WIDTH, Config.DRAW_AREA_HEIGHT + 2 * Config.BORDER_SIZE))
            .withPosition(position)
            .build().apply {
                gameComponent = Components.gameComponent<CharacterTile, Block<CharacterTile>>()
                        .withSize(contentSize)
                        .withVisibleSize(Sizes.from2DTo3D(contentSize))
                        .withGameArea(context.gameArea)
                        .build()
                addComponent(gameComponent)
            }

}
