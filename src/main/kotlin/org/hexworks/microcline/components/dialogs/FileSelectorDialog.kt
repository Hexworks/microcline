package org.hexworks.microcline.components.dialogs

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen


class FileSelectorDialog(screen: Screen) : Dialog(screen) {

    override val container = Components.panel()
            .withTitle("File")
            .withSize(40,25)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build()

}
