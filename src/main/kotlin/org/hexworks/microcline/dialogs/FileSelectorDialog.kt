package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.extensions.handleMouseEvents
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.extensions.onClosed
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.extensions.side
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_RELEASED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase
import javax.swing.JFileChooser


class FileSelectorDialog(screen: Screen,
                         private val context: EditorContext) : BaseDialog(screen) {

    // TODO: use service
    override val content = Components.panel()
            .withSize(34, 12)
            .withDecorations(box(boxType = BoxType.DOUBLE, title = "File"))
            .build().apply {
                val filePath = Components.textArea()
                        .withPosition(Position.offset1x1())
                        .withSize(30, 5)
                        .withText(context.currentFile.map { it.absolutePath }.orElse(Config.NONAME_FILE))
                        .build().apply {
                            isDisabled = true
                            handleMouseEvents(MOUSE_RELEASED, UIEventPhase.TARGET) {
                                JFileChooser().also { fc ->
                                    if (fc.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION) {
                                        text = fc.selectedFile.absolutePath
                                        context.currentFile = Maybe.of(fc.selectedFile)
                                    }
                                }
                                Processed
                            }
                        }
                addComponent(filePath)

                addComponent(Components.button()
                        .withPosition(1, 7)
                        .withText("New")
                        .withDecorations(side())
                        .build().apply {
                            processComponentEvents(ACTIVATED) {
                                println("new: reset state")
                                val modal = ConfirmationDialog(screen).apply {
                                    root.onClosed {
                                        when (it) {
                                            ConfirmationDialog.Yes -> TODO()
                                            ConfirmationDialog.No -> TODO()
                                        }
                                    }
                                }
                                screen.openModal(modal)
                                filePath.text = Config.NONAME_FILE
                            }
                        })

                addComponent(Components.button()
                        .withPosition(13, 7)
                        .withText("Open")
                        .withDecorations(side())
                        .build().apply {
                            processComponentEvents(ACTIVATED) {
                                println("open: load state")
                            }
                        })

                addComponent(Components.button()
                        .withPosition(25, 7)
                        .withText("Save")
                        .withDecorations(side())
                        .build().apply {
                            processComponentEvents(ACTIVATED) {
                                println("save: persist state")
                            }
                        })
            }

}
