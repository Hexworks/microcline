package org.codetome.microcline.screens

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.component.Button
import org.codetome.zircon.api.component.ColorTheme
import org.codetome.zircon.api.component.builder.ButtonBuilder
import org.codetome.zircon.api.component.builder.LabelBuilder
import org.codetome.zircon.api.component.builder.PanelBuilder
import org.codetome.zircon.api.screen.Screen
import org.codetome.zircon.api.terminal.Terminal
import org.codetome.zircon.internal.graphics.BoxType

class MainMenuScreen(
        private val screen: Screen,
        val newArtButton: Button,
        val loadArtButton: Button,
        val quitButton: Button) {

    fun display() = screen.display()

    companion object {

        private val MAIN_MENU_LABEL = "W E L C O M E   T O   M I C R O C L I N E"
        private val NEW_ART_BUTTON_LABEL = "N E W   A R T"
        private val LOAD_ART_BUTTON_LABEL = "L O A D   A R T"
        private val QUIT_BUTTON_LABEL = "Q U I T"

        private val MAIN_MENU_PANEL_WIDTH = 48
        private val MAIN_MENU_PANEL_HEIGHT = 9

        fun create(terminal: Terminal, theme: ColorTheme): MainMenuScreen {
            val (cols, rows) = terminal.getBoundableSize()
            val screen = TerminalBuilder.createScreenFor(terminal)
            val menuPosition = Position.of(
                    (cols - MAIN_MENU_PANEL_WIDTH) / 2,
                    (rows - MAIN_MENU_PANEL_HEIGHT) / 2)
            val mainMenuLabel = LabelBuilder.newBuilder()
                    .text(MAIN_MENU_LABEL)
                    .position(menuPosition.withRelativeRow(-3).withRelativeColumn(4))
                    .build()

            val menuPanel = PanelBuilder.newBuilder()
                    .boxType(BoxType.LEFT_RIGHT_DOUBLE)
                    .wrapInBox()
                    .position(menuPosition)
                    .size(Size.of(MAIN_MENU_PANEL_WIDTH, MAIN_MENU_PANEL_HEIGHT))
                    .build()

            val newArtButton = ButtonBuilder.newBuilder()
                    .text(NEW_ART_BUTTON_LABEL)
                    .position(Position.of((MAIN_MENU_PANEL_WIDTH - NEW_ART_BUTTON_LABEL.length) / 2 - 1, 1))
                    .build()

            val loadArtButton = ButtonBuilder.newBuilder()
                    .text(LOAD_ART_BUTTON_LABEL)
                    .position(Position.of((MAIN_MENU_PANEL_WIDTH - LOAD_ART_BUTTON_LABEL.length) / 2 - 1, 3))
                    .build()

            val quitButton = ButtonBuilder.newBuilder()
                    .text(QUIT_BUTTON_LABEL)
                    .position(Position.of((MAIN_MENU_PANEL_WIDTH - QUIT_BUTTON_LABEL.length) / 2 - 1, 5))
                    .build()


            screen.addComponent(mainMenuLabel)
            menuPanel.addComponent(newArtButton)
            menuPanel.addComponent(loadArtButton)
            menuPanel.addComponent(quitButton)


            screen.addComponent(menuPanel)
            screen.applyColorTheme(theme)

            return MainMenuScreen(
                    screen = screen,
                    newArtButton = newArtButton,
                    loadArtButton = loadArtButton,
                    quitButton = quitButton)
        }
    }
}
