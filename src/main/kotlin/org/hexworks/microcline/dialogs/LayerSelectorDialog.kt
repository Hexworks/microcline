package org.hexworks.microcline.dialogs

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class LayerSelectorDialog(screen: Screen) : Dialog(screen) {

    override val container = Components.panel()
            .withTitle("Layer")
            .withSize(20,15)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build()

}
