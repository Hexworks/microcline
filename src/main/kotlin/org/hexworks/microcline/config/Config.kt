package org.hexworks.microcline.config

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size


object Config {

    val TILESET = CP437TilesetResources.rexPaint14x14()
    // 80x45 -> 16:9 ratio
    const val DRAW_AREA_WIDTH: Int = 80
    const val DRAW_AREA_HEIGHT: Int = 45
    val DRAW_SIZE: Size = Size.create(DRAW_AREA_WIDTH, DRAW_AREA_HEIGHT)
    const val TOOLBELT_HEIGHT: Int = 1
    const val BORDER_SIZE: Int = 1
    const val WINDOW_WIDTH: Int = 2 * BORDER_SIZE + DRAW_AREA_WIDTH
    const val WINDOW_HEIGHT: Int = 4 * BORDER_SIZE + DRAW_AREA_HEIGHT + TOOLBELT_HEIGHT
    const val MAX_LAYERS: Int = 2
    const val NONAME_FILE: String = "<noname>"

    @Suppress("ConstantConditionIf")
    fun buildAppConfig(): AppConfig = AppConfigs.newConfig()
            .enableBetaFeatures()
            .withDefaultTileset(Config.TILESET)
            .withSize(Sizes.create(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT))
            .build()

}
