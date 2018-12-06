package org.hexworks.microcline.config

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.application.AppConfig
import java.awt.Toolkit


object Config {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenSizePercent = 0.95
    val tileset = CP437TilesetResources.rexPaint12x12()
    val windowWidth = screenSize.width.div(tileset.width).times(screenSizePercent).toInt()
    val windowHeight = screenSize.height.div(tileset.height).times(screenSizePercent).toInt()

    @Suppress("ConstantConditionIf")
    fun buildAppConfig(): AppConfig {
        val config = AppConfigs.newConfig()
                .enableBetaFeatures()
                .withDefaultTileset(tileset)
                .withSize(Sizes.create(windowWidth, windowHeight))

        return config.build()
    }
}
