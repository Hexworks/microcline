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

class EraseTest {

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

    @DisplayName("Given an erase command When executing it the result should have the touched positions erased")
    @Test
    fun shouldProperlyErase() {

        val result = Erase(listOf(Positions.create(1, 2), Positions.create(2, 2)))
                .execute(snapshot)

        assertThat(result).isEqualTo(
                DrawingSnapshot(
                        selectedTile = NEW_TILE,
                        selectedLayerIdx = 1,
                        layers = listOf(
                                DrawLayer("Layer 0"),
                                DrawLayer("Layer 1", tiles = mapOf(
                                        Positions.create(3, 4) to OLD_TILE)))))
    }

    @DisplayName("Given an erase command which erases already empty tiles When executing it the result should be the same")
    @Test
    fun shouldProperlyDoNothing() {

        val result = Erase(listOf(Positions.create(6, 7)))
                .execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
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
