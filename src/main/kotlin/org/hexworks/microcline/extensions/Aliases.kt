package org.hexworks.microcline.extensions

import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.game.GameComponent

typealias DrawLayersArea = GameArea<CharacterTile, Block<CharacterTile>>

typealias DrawComponent = GameComponent<CharacterTile, Block<CharacterTile>>
