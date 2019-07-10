package org.hexworks.microcline.config

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.Size

// TODO: create a data class for this so we can externalize it
object Config {

    val TILESET = CP437TilesetResources.rexPaint14x14()
    val THEME = ColorThemes.solarizedLightBlue()

    // 80x45 -> 16:9 ratio
    const val DRAW_AREA_WIDTH = 80
    const val DRAW_AREA_HEIGHT = 45
    val DRAWING_SIZE: Size = Sizes.create(DRAW_AREA_WIDTH, DRAW_AREA_HEIGHT)
    val DRAW_AREA_BACKGROUND = Tiles.newBuilder()
            .withBackgroundColor(ANSITileColor.BLACK)
            .withCharacter(' ')
            .buildCharacterTile()

    const val MAX_LAYERS = 10
    const val TOOLBELT_HEIGHT = 1
    const val BORDER_SIZE = 1
    const val WINDOW_WIDTH = 2 * BORDER_SIZE + DRAW_AREA_WIDTH
    const val WINDOW_HEIGHT = 4 * BORDER_SIZE + DRAW_AREA_HEIGHT + TOOLBELT_HEIGHT

    const val NONAME_FILE: String = "<noname>"
    val DRAW_AREA_SIZE = Size.create(WINDOW_WIDTH, DRAW_AREA_HEIGHT + 2 * BORDER_SIZE)


    fun buildAppConfig(): AppConfig = AppConfigs.newConfig()
            .enableBetaFeatures()
            .withTitle("Microcline")
            .withDefaultTileset(TILESET)
            .withSize(Sizes.create(WINDOW_WIDTH, WINDOW_HEIGHT))
            .build()

}
