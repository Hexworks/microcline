package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.dialogs.base.BaseDialog
import org.hexworks.microcline.dialogs.base.ConfirmationDialog
import org.hexworks.microcline.dialogs.result.No
import org.hexworks.microcline.dialogs.result.Yes
import org.hexworks.microcline.extensions.handleMouseEvents
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.extensions.onClosed
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.extensions.side
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType.ACTIVATED
import org.hexworks.zircon.api.uievent.MouseEventType.MOUSE_RELEASED
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.uievent.UIEventPhase
import java.io.File
import javax.swing.JFileChooser


class FileSelectorDialog(
        screen: Screen,
        selectedFile: Property<Maybe<File>>) : BaseDialog<Nothing>(screen, CONTENT_SIZE, TITLE) {

    override val content = Components.panel()
            .withSize(CONTENT_SIZE)
            .build().apply {
                val filePath = Components.textArea()
                        .withPosition(Position.offset1x1())
                        .withSize(30, 5)
                        .withText(selectedFile.value.map { it.absolutePath }.orElse(Config.NONAME_FILE))
                        .build().apply {
                            isDisabled = true
                            handleMouseEvents(MOUSE_RELEASED, UIEventPhase.TARGET) {
                                JFileChooser().also { fc ->
                                    if (fc.showDialog(null, "Select") == JFileChooser.APPROVE_OPTION) {
                                        text = fc.selectedFile.absolutePath
                                        selectedFile.value = Maybe.of(fc.selectedFile)
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
                                            Yes -> TODO()
                                            No -> TODO()
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


    companion object {

        private val CONTENT_SIZE = Sizes.create(34, 12)
        private const val TITLE = "File"

    }
}
