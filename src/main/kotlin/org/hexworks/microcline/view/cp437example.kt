//package org.hexworks.microcline.view
//
//object CP437CharsExample {
//
//
//    @JvmStatic
//    fun main(args: Array<String>) {
//
//        val tileGrid = SwingApplications.startTileGrid()
//
//        val screen = Screens.createScreenFor(tileGrid)
//
//        val cp437panel = PanelBuilder.newBuilder()
//                .size(Size.create(19, 19))
//                .position(Positions.create(2, 2))
//                .wrapWithBox()
//                .wrapWithShadow()
//                .boxType(BoxType.SINGLE)
//                .build()
//
//        val loader = CP437TileMetadataLoader(16, 16)
//
//        screen.addComponent(cp437panel)
//        screen.applyColorTheme(ColorThemeResource.AMIGA_OS.getTheme())
//
//        loader.fetchMetadata().forEach { char, meta ->
//            cp437panel.draw(Tiles.defaultTile().withCharacter(char), Positions.create(meta.x, meta.y)
//                    .plus(Position.offset1x1()))
//        }
//
//        screen.display()
//    }
//
//}