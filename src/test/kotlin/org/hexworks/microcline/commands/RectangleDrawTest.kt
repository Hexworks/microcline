package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a rectangle draw command")
class RectangleDrawTest {

    lateinit var snapshot: DrawingSnapshot

    @BeforeEach
    fun setUp() {
        snapshot = DrawingSnapshot(
                selectedTile = NEW_TILE,
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0"),
                        DrawLayer("Layer 1", tiles = mapOf(
                                Positions.create(1, 2) to OLD_TILE,
                                Positions.create(3, 4) to OLD_TILE))))
    }

    @DisplayName("When executing it the result should have the supplied positions overwritten with the selected tile")
    @Test
    fun shouldProperlyOverwrite() {

        val result = RectangleDraw(Positions.create(1, 2), Positions.create(3, 4))
                .execute(snapshot)

        assertThat(result).isEqualTo(
                DrawingSnapshot(
                        selectedTile = NEW_TILE,
                        selectedLayerIdx = 1,
                        layers = listOf(
                                DrawLayer("Layer 0"),
                                DrawLayer("Layer 1", tiles = mapOf(
                                        Positions.create(1, 2) to NEW_TILE,
                                        Positions.create(2, 2) to NEW_TILE,
                                        Positions.create(3, 2) to NEW_TILE,
                                        Positions.create(1, 3) to NEW_TILE,
                                        Positions.create(3, 3) to NEW_TILE,
                                        Positions.create(1, 4) to NEW_TILE,
                                        Positions.create(2, 4) to NEW_TILE,
                                        Positions.create(3, 4) to NEW_TILE)))))
    }

    companion object {

        val OLD_TILE = Tiles.newBuilder()
                .withBackgroundColor(ANSITileColor.BLACK)
                .withForegroundColor(ANSITileColor.RED)
                .withCharacter('x')
                .buildCharacterTile()

        val NEW_TILE = Tiles.newBuilder()
                .withBackgroundColor(ANSITileColor.BLUE)
                .withForegroundColor(ANSITileColor.GRAY)
                .withCharacter('y')
                .buildCharacterTile()
    }
}
