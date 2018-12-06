package org.hexworks.microcline.components

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer

// TODO: This should be a Fragment instead
// TODO: A Fragment is a reusable object which has a `root` Component and some additional view logic for
// TODO: interacting with it.
// TODO: we shouldn't derive from `Panel` here.
class DrawPanel(
        position: Position,
        size: Size,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .withTitle("Draw")
                .withSize(size)
                .withPosition(position)
                .withComponentRenderer(NoOpComponentRenderer())
                .build()
): Panel by panel {

    // TODO: if we need external access for something this can be a `val` instead
    // TODO: check here: https://kotlinlang.org/docs/reference/properties.html#getters-and-setters
    fun getPanel() = panel

}
