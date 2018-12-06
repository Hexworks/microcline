package org.hexworks.microcline.views

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.grid.TileGrid


abstract class BaseView(tileGrid: TileGrid) : View {

    final override val screen = Screens.createScreenFor(tileGrid)
    private val colorTheme = ColorThemes.newBuilder()
            .withPrimaryBackgroundColor(ANSITileColor.BLACK)
            .withPrimaryForegroundColor(ANSITileColor.WHITE)
            .withSecondaryBackgroundColor(ANSITileColor.BLACK)
            .withSecondaryForegroundColor(ANSITileColor.WHITE)
            .withAccentColor(ANSITileColor.BRIGHT_WHITE)
            .build()

    final override fun dock() {
        screen.applyColorTheme(colorTheme)
        screen.display()
    }
}
