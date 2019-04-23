package org.hexworks.microcline.data

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.zircon.api.DrawSurfaces
import org.hexworks.zircon.api.behavior.Drawable
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.DrawSurface
import org.hexworks.zircon.api.graphics.TileGraphics

class DrawLayer(val size: Size,
                initialLabel: String,
                initialLocked: Boolean = false,
                initialVisible: Boolean = true,
                initialSelected: Boolean = false) : Drawable {

    val labelProperty = createPropertyFrom(initialLabel)
    val lockedProperty = createPropertyFrom(initialLocked)
    val visibleProperty = createPropertyFrom(initialVisible)
    val selectedProperty = createPropertyFrom(initialSelected)

    var label: String by labelProperty.asDelegate()
    var isLocked: Boolean by lockedProperty.asDelegate()
    var isVisible: Boolean by visibleProperty.asDelegate()
    var isSelected: Boolean by selectedProperty.asDelegate()

    private val graphics = DrawSurfaces.tileGraphicsBuilder()
            .withSize(size)
            .build()

    override fun drawOnto(surface: DrawSurface, position: Position) {
        graphics.drawOnto(surface, position)
    }

    fun clear() {
        if (lockedProperty.value.not()) {
            graphics.clear()
        }
    }

    fun draw(tileGraphics: TileGraphics) {
        if (lockedProperty.value.not()) {
            graphics.draw(tileGraphics)
        }
    }
}
