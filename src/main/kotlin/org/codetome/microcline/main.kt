package org.codetome.microcline

import org.codetome.microcline.screens.MainMenuScreen
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.resource.ColorThemeResource
import org.codetome.zircon.api.resource.PhysicalFontResource
import java.awt.Toolkit
import java.util.function.Consumer

private val THEME = ColorThemeResource.SOLARIZED_DARK_YELLOW.getTheme()
private val FONT = PhysicalFontResource.VT323.toFont()

fun main(args: Array<String>) {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val columns = screenSize.getWidth() / FONT.getWidth()
    val rows = screenSize.getHeight() / FONT.getHeight()
    val terminalSize = Size.of(columns.toInt(), rows.toInt())

    val terminal = TerminalBuilder.newBuilder()
            .font(FONT)
            .fullScreen()
            .initialTerminalSize(terminalSize)
            .build()

    val mainMenu = MainMenuScreen.create(terminal, THEME)

    mainMenu.quitButton.onMouseReleased(Consumer {
        System.exit(0)
    })

    mainMenu.display()
}
