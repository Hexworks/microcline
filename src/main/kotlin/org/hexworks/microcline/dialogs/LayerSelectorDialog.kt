package org.hexworks.microcline.dialogs

import org.hexworks.microcline.context.EditorContext
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class LayerSelectorDialog(screen: Screen,
                          private val context: EditorContext) : BaseDialog(screen) {

    override val content = Components.panel()
            .withTitle("Edit Layers")
            .withSize(30, 15)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {

            }


}
