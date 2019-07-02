package org.hexworks.microcline.data

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.zircon.api.DrawSurfaces
import org.hexworks.zircon.api.behavior.Drawable
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.graphics.TileGraphics
import org.hexworks.zircon.api.graphics.TileImage

/**
 * A [DrawLayer] is a [Drawable] which acts as a layer which the user
 * can draw onto. It works in a similar way as *layers* work in common
 * image editors: it can be *locked* which prohibits modification of any
 * kind, *selected* which enables *drawing* onto the layer and *visible*
 * which enables displaying the layer.
 */
class DrawLayer(val size: Size,
                initialLabel: String,
                initialLocked: Boolean = false,
                initialVisible: Boolean = true,
                initialSelected: Boolean = false) {

    val id = Identifier.randomIdentifier()

    val nameProperty = createPropertyFrom(initialLabel)
    val lockedProperty = createPropertyFrom(initialLocked)
    val visibleProperty = createPropertyFrom(initialVisible)
    val selectedProperty = createPropertyFrom(initialSelected)

    var name: String by nameProperty.asDelegate()

    /**
     * Tells whether this layer is locked. When a [DrawLayer] is
     * locked any kind of modification is prohibited.
     */
    var isLocked: Boolean by lockedProperty.asDelegate()

    /**
     * Tells whether this layer is visible. When a [DrawLayer] is
     * visible the [drawOnto] operation is enabled.
     */
    var isVisible: Boolean by visibleProperty.asDelegate()

    /**
     * Tells whether this layer is selected. When a [DrawLayer] is
     * selected it enables the [draw] operation.
     */
    var isSelected: Boolean by selectedProperty.asDelegate()

    /**
     * The current content of this [DrawLayer] as a [TileImage].
     */
    val content: TileImage
        get() = graphics.toTileImage()

    private val graphics = DrawSurfaces.tileGraphicsBuilder()
            .withSize(size)
            .build()

    fun clear() {
        if (isLocked.not()) {
            graphics.clear()
        }
    }

    fun draw(tileGraphics: TileGraphics) {
        if (isLocked.not()) {
            graphics.draw(tileGraphics)
        }
    }
}
