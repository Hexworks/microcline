package org.hexworks.microcline.components.dialogs

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.state.State
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.kotlin.onMouseReleased
import org.hexworks.zircon.api.screen.Screen
import javax.swing.JFileChooser


class FileSelectorDialog(screen: Screen) : Dialog(screen) {

    override val container = Components.panel()
            .withTitle("File")
            .withSize(34,12)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {
                addComponent(Components.textArea()
                        .withPosition(Position.offset1x1())
                        .withSize(30,5)
                        .withText(if (!State.file.isEmpty()) State.file.get().absolutePath else Config.NONAME_FILE)
                        .build().apply {
                            disable()
                            onMouseReleased {
                                JFileChooser().also { fc ->
                                    if (fc.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION) {
                                        text = fc.selectedFile.absolutePath
                                        State.file = Maybe.of(fc.selectedFile)
                                    }
                                }
                            }
                        })

                addComponent(Components.button()
                        .withPosition(1, 7)
                        .withText("New")
                        .wrapSides(true)
                        .build().apply {
                            onMouseReleased {
                                println("new: reset state")
                            }
                        })

                addComponent(Components.button()
                        .withPosition(13, 7)
                        .withText("Open")
                        .wrapSides(true)
                        .build().apply {
                            onMouseReleased {
                                println("open: load state")
                            }
                        })

                addComponent(Components.button()
                        .withPosition(25, 7)
                        .withText("Save")
                        .wrapSides(true)
                        .build().apply {
                            onMouseReleased {
                                println("save: persist state")
                            }
                        })
            }

}
