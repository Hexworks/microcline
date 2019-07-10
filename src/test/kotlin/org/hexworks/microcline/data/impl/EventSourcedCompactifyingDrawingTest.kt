package org.hexworks.microcline.data.impl

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.commands.Command
import org.hexworks.microcline.commands.Erase
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.data.Position
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given an event sourced compactifying drawing")
class EventSourcedCompactifyingDrawingTest {

    lateinit var target: EventSourcedCompactifyingDrawing

    @BeforeEach
    fun setUp() {
        target = EventSourcedCompactifyingDrawing(
                historySize = 2,
                baseState = INITIAL_SNAPSHOT)
    }

    @DisplayName("When sending a command and fetching the state it should be correct")
    @Test
    fun shouldExecuteCommandProperly() {
        target.execute(SetTileCommand(Positions.create(0, 0)))
        val result = target.state

        assertThat(result.selectedLayer.tiles).isEqualTo(mapOf(Positions.create(0, 0) to TILE))
    }

    @DisplayName("When sending a command and checking the diff it should be correct")
    @Test
    fun shouldReturnDiffProperly() {
        val result = target.execute(SetTileCommand(Positions.create(0, 0)))

        assertThat(result).isEqualTo(mapOf(Positions.create(0, 0) to TILE))
    }

    @DisplayName("When sending a command and checking the diff it should be correct")
    @Test
    fun shouldReturnDiffProperlyWhenErasing() {
        target.execute(SetTileCommand(Positions.create(0, 0)))
        val result = target.execute(Erase(listOf(Positions.create(0, 0))))

        assertThat(result).isEqualTo(mapOf(Positions.create(0, 0) to Tiles.empty()))
    }

    companion object {

        class SetTileCommand(private val position: Position) : Command {

            override val name = "Set Tile"

            override fun toString() = "$name at $position"

            override fun execute(state: DrawingSnapshot): DrawingSnapshot {
                val oldTiles = state.selectedLayer.tiles
                return state.withSelectedLayer(state.selectedLayer.copy(
                        tiles = oldTiles.plus(position to state.selectedTile)))
            }
        }

        private val TILE = Tiles.newBuilder()
                .withBackgroundColor(ANSITileColor.BLACK)
                .withForegroundColor(ANSITileColor.RED)
                .withCharacter('x')
                .buildCharacterTile()

        val INITIAL_SNAPSHOT = DrawingSnapshot(selectedTile = TILE)

    }
}
