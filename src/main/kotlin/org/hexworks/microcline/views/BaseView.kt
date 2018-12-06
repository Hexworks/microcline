package org.hexworks.microcline.views

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.grid.TileGrid


abstract class BaseView(tileGrid: TileGrid) : View {

    final override val screen = Screens.createScreenFor(tileGrid)
    private val colorTheme = ColorThemes.zenburnVanilla()

    final override fun dock() {
        screen.applyColorTheme(colorTheme)
        screen.display()
    }
}
