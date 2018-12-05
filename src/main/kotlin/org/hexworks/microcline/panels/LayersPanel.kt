package org.hexworks.microcline.panels

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer

const val LAYERS_P_SIZE_X = 16
const val LAYERS_P_SIZE_Y = 13

// TODO: This should be a Fragment instead
// TODO: A Fragment is a reusable object which has a `root` Component and some additional view logic for
// TODO: interacting with it.
// TODO: we shouldn't derive from `Panel` here.
class LayersPanel(
        position: Position,
        private val panel: Panel = Components.panel()
                .wrapWithBox(true)
                .withTitle("Layers")
                .withSize(Sizes.create(LAYERS_P_SIZE_X, LAYERS_P_SIZE_Y).plus(Sizes.create(2, 2)))
                .withPosition(position)
                .withComponentRenderer(NoOpComponentRenderer())
                .build()
): Panel by panel {

    fun getPanel() = panel

}
