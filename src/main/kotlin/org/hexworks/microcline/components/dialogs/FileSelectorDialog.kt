package org.hexworks.microcline.components.dialogs

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.extensions.onMouseEvent
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onClosed
import org.hexworks.zircon.api.extensions.onComponentEvent
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
            .withTitle("File")
            .withSize(34, 12)
            .withBoxType(BoxType.DOUBLE)
            .wrapWithBox()
            .build().apply {
                val filePath = Components.textArea()
                        .withPosition(Position.offset1x1())
                        .withSize(30, 5)
                        .withText(context.currentFile.map { it.absolutePath }.orElse(Config.NONAME_FILE))
                        .build().apply {
                            disable()
                            onMouseEvent(MOUSE_RELEASED, UIEventPhase.TARGET) {
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
                        .wrapSides(true)
                        .build().apply {
                            onComponentEvent(ACTIVATED) {
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
                                context.reset()
                                filePath.text = Config.NONAME_FILE
                                Processed
                            }
                        })

                addComponent(Components.button()
                        .withPosition(13, 7)
                        .withText("Open")
                        .wrapSides(true)
                        .build().apply {
                            onComponentEvent(ACTIVATED) {
                                println("open: load state")
                                Processed
                            }
                        })

                addComponent(Components.button()
                        .withPosition(25, 7)
                        .withText("Save")
                        .wrapSides(true)
                        .build().apply {
                            onComponentEvent(ACTIVATED) {
                                println("save: persist state")
                                Processed
                            }
                        })
            }

}
