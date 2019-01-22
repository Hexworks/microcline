package org.hexworks.microcline.playground

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.*
import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.graphics.Layer
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.input.MouseActionType
import org.hexworks.zircon.api.kotlin.onMouseAction
import org.hexworks.zircon.api.kotlin.onSelection
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.api.shape.LineFactory

object DrawingExample {

    private const val TOOLS_WIDTH = 15
    private const val LAYERS = 5
    private val TEST_TILE = Tiles.newBuilder()
            .withBackgroundColor(ANSITileColor.GREEN)
            .withForegroundColor(ANSITileColor.RED)
            .withCharacter(Symbols.DELTA)
            .buildCharacterTile()
    private val EMPTY_TILE = Tiles.empty()
    private val BLACK_TILE = Tiles.newBuilder()
            .withBackgroundColor(ANSITileColor.BLACK)
            .buildCharacterTile()
    private val EMPTY_BLOCK = Blocks.newBuilder<Tile>()
            .withEmptyTile(EMPTY_TILE)
            .withLayers(EMPTY_TILE)
            .build()
    private val BLACK_BLOCK = Blocks.newBuilder<Tile>()
            .withEmptyTile(BLACK_TILE)
            .withLayers(BLACK_TILE)
            .build()

    @JvmStatic
    fun main(args: Array<String>) {

        val app = SwingApplications.startApplication(AppConfigs.newConfig()
                .enableBetaFeatures()
                .build())

        val size = Sizes.create3DSize(
                xLength = app.tileGrid.width - 2 - TOOLS_WIDTH,
                yLength = app.tileGrid.height - 2,
                zLength = 100)

        app.dock(DrawView(DrawController(size)))
    }

    data class Context(val chosenLayer: Layer,
                       val gameAreaOffset: Position,
                       val layerLevel: Int,
                       val chosenTile: Tile)

    interface DrawCommand {

        fun execute(context: Context, gameArea: GameArea<Tile, Block<Tile>>, mouseAction: MouseAction)
    }

    class FreeDrawCommand : DrawCommand {
        override fun execute(context: Context, gameArea: GameArea<Tile, Block<Tile>>, mouseAction: MouseAction) {
            val (layer, offset, _, tile) = context
            if (mouseAction.actionType == MouseActionType.MOUSE_RELEASED) {
                layer.setTileAt(mouseAction.position - offset, tile)
            }
        }

    }

    class DrawLineCommand : DrawCommand {

        private var maybePressed = Maybe.empty<Position>()
        private var maybeTemp = Maybe.empty<Layer>()

        override fun execute(context: Context, gameArea: GameArea<Tile, Block<Tile>>, mouseAction: MouseAction) {
            val (layer, offset, level, tile) = context
            when(mouseAction.actionType) {
                MouseActionType.MOUSE_PRESSED -> {
                    val temp = Layers.newBuilder()
                            .withSize(layer.size)
                            .build()
                    maybePressed = Maybe.of(mouseAction.position - context.gameAreaOffset)
                    maybeTemp = Maybe.of(temp)
                    gameArea.pushOverlayAt(temp, level)
                }
                MouseActionType.MOUSE_RELEASED -> {
                    maybePressed.map { pressedAt ->
                        val tempLayer = maybeTemp.get()
                        val pos = mouseAction.position - offset
                        if(pos != pressedAt) {
                            tempLayer.clear()
                            LineFactory.buildLine(pressedAt, pos).positions().forEach {
                                tempLayer.setTileAt(it, tile)
                            }
                            layer.draw(tempLayer)
                        }
                        gameArea.removeOverlay(tempLayer, level)
                    }
                }
                MouseActionType.MOUSE_DRAGGED -> {
                    maybePressed.map { pressedAt ->
                        val tempLayer = maybeTemp.get()
                        val pos = mouseAction.position - offset
                        if(pos != pressedAt) {
                            tempLayer.clear()
                            LineFactory.buildLine(pressedAt, pos).positions().forEach {
                                tempLayer.setTileAt(it, tile)
                            }
                        }
                    }
                }
                else -> {}
            }
        }

    }

    enum class DrawOption(val drawCommand: DrawCommand) {
        FREE(FreeDrawCommand()),
        LINE(DrawLineCommand());
    }

    class DrawController(drawAreaSize: Size3D) {
        val gameArea = GameAreaBuilder<Tile, Block<Tile>>()
                .withActualSize(drawAreaSize)
                .withVisibleSize(drawAreaSize)
                .withLayersPerBlock(1)
                .withDefaultBlock(EMPTY_BLOCK)
                .build()

        private val overlays = (0 until LAYERS).map {
            it to Layers.newBuilder().withSize(drawAreaSize.to2DSize()).build()
        }.toMap()
        private var chosenCommand = DrawOption.FREE.drawCommand
        private var chosenLayer = 0 to overlays[0]!!

        init {
            gameArea.actualSize().to2DSize().fetchPositions().forEach {
                gameArea.setBlockAt(Positions.from2DTo3D(it), BLACK_BLOCK)
            }
//            drawing.pushOverlayAt(chosenLayer.second, chosenLayer.first)
            overlays.map { gameArea.pushOverlayAt(it.value, it.key) }
        }

        fun chooseLayer(idx: Int) {
//            overlays.keys.forEach { drawing.popOverlayAt(it) }
//            overlays.map {
//                if (it.key != idx) {
//                    drawing.pushOverlayAt(it.value, it.key)
//                }
//            }
            chosenLayer = idx to overlays[idx]!!
//            drawing.pushOverlayAt(chosenLayer.second, chosenLayer.first)

//            drawing.popOverlayAt(chosenLayer.first)
//            chosenLayer = idx to overlays[idx]!!
//            drawing.pushOverlayAt(chosenLayer.second, chosenLayer.first)
        }

        fun useChosenTool(mouseAction: MouseAction) {
            chosenCommand.execute(
                    context = Context(
                            chosenLayer = chosenLayer.second,
                            gameAreaOffset = Positions.create(TOOLS_WIDTH + 1, 1),
                            layerLevel = chosenLayer.first,
                            chosenTile = Tiles.newBuilder()
                                    .withCharacter((chosenLayer.first+65).toChar())
                                    .withForegroundColor(TileColors.create(255,0,0))
                                    .withBackgroundColor(TileColors.create(0,255,255))
                                    .build()),
                    gameArea = gameArea,
                    mouseAction = mouseAction)
        }

        fun chooseTool(tool: DrawOption) {
            chosenCommand = tool.drawCommand
        }
    }

    class DrawView(private val controller: DrawController) : BaseView() {

        override val theme = ColorThemes.arc()

        override fun onDock() {

            val tools = Components.panel()
                    .withSize(TOOLS_WIDTH, 4)
                    .wrapWithBox()
                    .withTitle("Tools")
                    .build().apply {
                        val chooseTool = Components.radioButtonGroup()
                                .withSize(TOOLS_WIDTH - 2, 2)
                                .build().apply {
                                    DrawOption.values().forEachIndexed { index, drawOption ->
                                        val opt = addOption(drawOption.name, drawOption.name)
                                        if (index == 0) {
                                            opt.isSelected = true
                                        }
                                    }
                                    onSelection {
                                        controller.chooseTool(DrawOption.valueOf(it.key))
                                    }
                                }
                        addComponent(chooseTool)
                    }

            val layers = Components.panel()
                    .withSize(TOOLS_WIDTH, LAYERS + 2)
                    .wrapWithBox()
                    .withTitle("Layers")
                    .withPosition(0, tools.height)
                    .build().apply {
                        val chooseLayer = Components.radioButtonGroup()
                                .withSize(TOOLS_WIDTH - 2, LAYERS)
                                .build().apply {
                                    repeat(LAYERS) { idx ->
                                        val opt = addOption(idx.toString(), idx.toString())
                                        if (idx == 0) {
                                            opt.isSelected = true
                                        }
                                    }
                                    onSelection {
                                        controller.chooseLayer(it.key.toInt())
                                    }
                                }
                        addComponent(chooseLayer)
                    }

            val drawArea = Components.panel()
                    .withSize(screen.size.withRelativeWidth(-TOOLS_WIDTH))
                    .withPosition(TOOLS_WIDTH, 0)
                    .wrapWithBox()
                    .withTitle(("Drawer Surface"))
                    .build().apply {
                        val gc = Components.gameComponent<Tile, Block<Tile>>()
                                .withSize(contentSize)
                                .withVisibleSize(Sizes.from2DTo3D(contentSize))
                                .withGameArea(controller.gameArea)
                                .build().apply {
                                    onMouseAction { mouseAction ->
                                        controller.useChosenTool(mouseAction)
                                    }
                                }
                        addComponent(gc)
                    }


            screen.addComponent(tools)
            screen.addComponent(layers)
            screen.addComponent(drawArea)
        }
    }
}
