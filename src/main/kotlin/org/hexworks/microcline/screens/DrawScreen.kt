//package org.hexworks.microcline.screens
//
//import org.hexworks.zircon.api.builder.component.LabelBuilder
//import org.hexworks.zircon.api.component.Label
//import org.hexworks.zircon.api.data.Position
//import org.hexworks.zircon.api.resource.CP437TilesetResource
//import org.hexworks.zircon.api.screen.Screen
//
//
//private const val DRAW_MENU_PANEL_WIDTH = 16
//
//class DrawScreen(private val screen: Screen) {
//    fun display() = screen.display()
//
//    companion object {
////        private fun generateGlyphButton(num: Int): Button {
////            return ButtonBuilder.newBuilder()
////                    .text(CP437TilesetResource.convertCp437toUnicode(num).toString())
////                    .position(Position.of(num % DRAW_MENU_PANEL_WIDTH, num / DRAW_MENU_PANEL_WIDTH))
////                    .build()
////        }
//
//        private fun generateGlyphLabel(num: Int): Label {
//            return LabelBuilder.newBuilder()
//                    .text(CP437TilesetResource.convertCp437toUnicode(num).toString())
//                    .position(Position.of(num % DRAW_MENU_PANEL_WIDTH, num / DRAW_MENU_PANEL_WIDTH))
//                    .build()
//        }
//
//        fun create(terminal: Terminal, theme: ColorTheme): DrawScreen {
//            val screen = TerminalBuilder.createScreenFor(terminal)
//            val glyphPanel = PanelBuilder.newBuilder()
//                    .boxType(BoxType.LEFT_RIGHT_DOUBLE)
//                    .wrapInBox()
//                    .position(Position(0, 0))
//                    .size(Size.of(DRAW_MENU_PANEL_WIDTH, 16))
//                    .build()
//
//            val gp = TextBoxBuilder.newBuilder()
//                    .position(Position(0, 0))
//                    .size(Size.of(DRAW_MENU_PANEL_WIDTH, 16))
//                    .build()
//
//            gp.setText(
//                    (0..255).forEach {
//                        CP437TilesetResource.convertCp437toUnicode(it)
//                    }.toString()
//            )
//
////            (0..255).forEach {
////                glyphPanel.addComponent(generateGlyphLabel(it))
////                screen.setCharacterAt(Position.of(it % DRAW_MENU_PANEL_WIDTH, it / DRAW_MENU_PANEL_WIDTH), CP437TilesetResource.convertCp437toUnicode(it))
////                terminal.putCharacter(CP437TilesetResource.convertCp437toUnicode(it))
////            }
//
////            glyphPanel.addComponent(generateGlyphButton(200))
//
////            screen.addComponent(glyphPanel)
//
//            screen.addComponent(gp)
//            screen.applyColorTheme(theme)
//
//            return DrawScreen(screen = screen)
//        }
//    }
//}
