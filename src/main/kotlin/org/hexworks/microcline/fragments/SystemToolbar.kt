package org.hexworks.microcline.fragments

import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.ApplicationContext
import org.hexworks.microcline.dialogs.FileSelectorDialog
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.extensions.box
import org.hexworks.zircon.api.screen.Screen


class SystemToolbar(screen: Screen,
                    private val context: ApplicationContext) : Fragment {

    override val root = Components.hbox()
            .withSize(Size.create(
                    width = Config.WINDOW_WIDTH,
                    height = Config.TOOLBELT_HEIGHT + 2 * Config.BORDER_SIZE))
            .build()

    private val fileText = Components.label()
            .withSize(Size.create(60, 1))
            .withText(Config.NONAME_FILE)
            .build().apply {
                textProperty.updateFrom(context.selectedFileProperty) {
                    it.map { file -> file.name }.orElse(Config.NONAME_FILE)
                }
            }

    private val fileTool = Tool(
            buttonLabel = "File",
            visualization = fileText,
            activationHandler = { screen.openModal(FileSelectorDialog(screen, context.selectedFileProperty)) })

    private val settingsButton = Components.button()
            .withSize(Size.create(10, 1))
            .withText("Settings")
            .build().apply {  }

    init {
        root.addFragment(fileTool)
        root.addComponent(Components.panel()
                .withSize(12, 3)
                .withDecorations(box())
                .build().apply {
                    addComponent(settingsButton)
                })
    }
}
