package org.hexworks.microcline

import org.hexworks.microcline.views.DrawView
import org.hexworks.microcline.views.View
import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.data.Size
import java.awt.Toolkit
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.kotlin.onInput


fun main(args: Array<String>) {

    val tileSize = 12
    val screenSize = Toolkit.getDefaultToolkit().screenSize

    val windowWidth = screenSize.width.div(tileSize).times(0.95).toInt()
    val windowHeight = screenSize.height.div(tileSize).times(0.95).toInt()

    val grid = SwingApplications.startTileGrid(AppConfigs.newConfig()
            .withSize(Size.create(windowWidth, windowHeight))
            .withDefaultTileset(CP437TilesetResources.rexPaint12x12())
            .build())


    var currentView: View = DrawView(grid)

    // TODO: we don't need this anymore, Screens and Components cover this functionality
    // TODO: see: https://github.com/Hexworks/caves-of-zircon/blob/master/src/main/kotlin/org/hexworks/cavesofzircon/view/StartView.kt#L31
    grid.onInput { input ->
        val oldView = currentView
        currentView = currentView.respondToUserInput(input)
        if (oldView !== currentView) {
            currentView.display()
        }
    }

    currentView.display()
}
