package org.hexworks.microcline.dialogs

import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.fragments.LayerEditor
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class LayerEditorDialog(screen: Screen,
                        private val context: EditorContext) : BaseDialog(screen) {

    override val content = Components.panel()
            .withTitle("Edit Layers")
            .withSize(52, 20)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {
                val name = Components.header().withSize(9, 1).withText("Name").build()
                val select = Components.header().withPosition(Positions.topRightOf(name)).withSize(7, 1).withText("Select").build()
                val visible = Components.header().withPosition(Positions.topRightOf(select)).withSize(7, 1).withText("Visible").build()
                val lock = Components.header().withPosition(Positions.topRightOf(visible)).withSize(6, 1).withText("Lock").build()
                val move = Components.header().withPosition(Positions.topRightOf(lock)).withSize(6, 1).withText("Move").build()

                addComponent(name)
                addComponent(select)
                addComponent(visible)
                addComponent(lock)
                addComponent(move)
                context.currentLayers.forEachIndexed { idx, layer ->
                    addFragment(LayerEditor(
                            position = Positions.create(0, idx + 1),
                            layer = layer,
                            context = context))
                }
            }


}
