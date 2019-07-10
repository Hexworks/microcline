package org.hexworks.microcline

import org.hexworks.microcline.adapters.DrawAdapter
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.ApplicationContext
import org.hexworks.microcline.data.Palettes
import org.hexworks.microcline.views.DrawView
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.graphics.Symbols

fun main() {
    val app = SwingApplications
            .startApplication(Config.buildAppConfig())
    val context = ApplicationContext(
            initialTile = Tiles.newBuilder()
                    .withCharacter(Symbols.FACE_WHITE)
                    .withForegroundColor(Palettes.XTERM_256.colors.first())
                    .withBackgroundColor(Palettes.XTERM_256.colors.last())
                    .buildCharacterTile()
    )

    DrawAdapter(
            drawLayersArea = context.drawLayersArea,
            drawComponent = context.drawingComponent,
            drawing = context.selectedDrawing,
            selectedModeProperty = context.selectedDrawModeProperty)

    app.dock(DrawView(context))
}
