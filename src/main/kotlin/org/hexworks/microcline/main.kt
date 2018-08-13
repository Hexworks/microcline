package org.hexworks.microcline

import org.hexworks.microcline.view.View
import org.hexworks.microcline.view.WelcomeView
import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.Size
import java.awt.Toolkit

fun main(args: Array<String>) {

    val tileSize = 16
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    val windowWidth = screenSize.width.div(tileSize).times(0.95).toInt()
    val windowHeight = screenSize.height.div(tileSize).times(0.95).toInt()

    val grid = SwingApplications.startTileGrid(AppConfigs.newConfig()
            .defaultSize(Size.create(windowWidth, windowHeight))
            .defaultTileset(CP437TilesetResources.wanderlust16x16())
            .debugMode(true)
            .build())


    var currentView: View = WelcomeView(grid)

    grid.onInput { input ->
        currentView = currentView.respondToUserInput(input)
        currentView.display()
    }

    currentView.display()
}
