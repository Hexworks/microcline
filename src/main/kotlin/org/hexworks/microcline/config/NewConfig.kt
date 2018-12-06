package org.hexworks.microcline.config

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.application.AppConfig

object NewConfig {

    val TILESET = CP437TilesetResources.rexPaint12x12()
    const val DRAW_AREA_WIDTH: Int = 80
    const val DRAW_AREA_HEIGHT: Int = 50
    const val TOOLBELT_HEIGHT: Int = 1
    const val BORDER_SIZE: Int = 1
    const val WINDOW_WIDTH: Int = 2 * BORDER_SIZE + DRAW_AREA_WIDTH
    const val WINDOW_HEIGHT: Int = 4 * BORDER_SIZE + DRAW_AREA_HEIGHT + TOOLBELT_HEIGHT

    @Suppress("ConstantConditionIf")
    fun buildAppConfig(): AppConfig {
        val config = AppConfigs.newConfig()
                .enableBetaFeatures()
                .withDefaultTileset(NewConfig.TILESET)
                .withSize(Sizes.create(NewConfig.WINDOW_WIDTH, NewConfig.WINDOW_HEIGHT))

        return config.build()
    }
}
