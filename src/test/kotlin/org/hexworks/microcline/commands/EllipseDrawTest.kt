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

@DisplayName("Given an ellipse draw command")
class EllipseDrawTest {
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

        val result = EllipseDraw(Positions.create(5, 5), Positions.create(10, 10))
                .execute(snapshot)

        assertThat(result).isEqualTo(
                DrawingSnapshot(
                        selectedTile = NEW_TILE,
                        selectedLayerIdx = 1,
                        layers = listOf(
                                DrawLayer("Layer 0"),
                                DrawLayer("Layer 1", tiles = mapOf(
                                        Positions.create(1, 2) to NEW_TILE,
                                        Positions.create(3, 4) to OLD_TILE,
                                        Positions.create(5, 10) to NEW_TILE,
                                        Positions.create(5, 0) to NEW_TILE,
                                        Positions.create(6, 10) to NEW_TILE,
                                        Positions.create(4, 10) to NEW_TILE,
                                        Positions.create(6, 0) to NEW_TILE,
                                        Positions.create(4, 0) to NEW_TILE,
                                        Positions.create(7, 10) to NEW_TILE,
                                        Positions.create(3, 10) to NEW_TILE,
                                        Positions.create(7, 0) to NEW_TILE,
                                        Positions.create(3, 0) to NEW_TILE,
                                        Positions.create(8, 9) to NEW_TILE,
                                        Positions.create(2, 9) to NEW_TILE,
                                        Positions.create(8, 1) to NEW_TILE,
                                        Positions.create(2, 1) to NEW_TILE,
                                        Positions.create(10, 5) to NEW_TILE,
                                        Positions.create(0, 5) to NEW_TILE,
                                        Positions.create(10, 6) to NEW_TILE,
                                        Positions.create(0, 6) to NEW_TILE,
                                        Positions.create(10, 4) to NEW_TILE,
                                        Positions.create(0, 4) to NEW_TILE,
                                        Positions.create(10, 7) to NEW_TILE,
                                        Positions.create(0, 7) to NEW_TILE,
                                        Positions.create(10, 3) to NEW_TILE,
                                        Positions.create(0, 3) to NEW_TILE,
                                        Positions.create(9, 8) to NEW_TILE,
                                        Positions.create(1, 8) to NEW_TILE,
                                        Positions.create(9, 2) to NEW_TILE)))))
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
