package org.hexworks.microcline.data

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.expression.not
import org.hexworks.zircon.api.DrawSurfaces
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.builder.graphics.LayerBuilder
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.graphics.TileGraphics

class DrawLayer(val size: Size,
                initialLabel: String,
                initialLocked: Boolean = false,
                initialVisible: Boolean = true,
                initialSelected: Boolean = false) {

    val labelProperty = createPropertyFrom(initialLabel)
    val lockedProperty = createPropertyFrom(initialLocked)
    val visibleProperty = createPropertyFrom(initialVisible)
    val selectedProperty = createPropertyFrom(initialSelected)

    var label: String by labelProperty.asDelegate()
    var isLocked: Boolean by lockedProperty.asDelegate()
    var isVisible: Boolean by visibleProperty.asDelegate()
    var isSelected: Boolean by selectedProperty.asDelegate()

    val layer: Layer = LayerBuilder.newBuilder()
            .withTileGraphics(DrawSurfaces.tileGraphicsBuilder()
                    .withSize(size)
                    .build())
            .build()

    init {
        layer.hiddenProperty.bind(visibleProperty.not())
    }

    fun clear() {
        if (lockedProperty.value.not()) {
            layer.clear()
        }
    }

    fun draw(tileGraphics: TileGraphics) {
        if (lockedProperty.value.not()) {
            layer.draw(tileGraphics)
        }
    }
}
