package org.hexworks.microcline.data

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a drawing snapshot")
class DrawingSnapshotTest {


    @DisplayName("When diff is called with old state it should properly create the diff")
    @Test
    fun shouldDiffForwards() {

        val result = NEW_SNAPSHOT.diff(OLD_SNAPSHOT)

        assertThat(result).isEqualTo(mapOf(
                Positions.create(0, 0) to Tiles.empty(),
                Positions.create(2, 1) to NEW_TILE,
                Positions.create(1, 1) to NEW_TILE))
    }

    @DisplayName("When diff is called with new state it should properly create the diff")
    @Test
    fun shouldDiffBackwards() {

        val result = OLD_SNAPSHOT.diff(NEW_SNAPSHOT)

        assertThat(result).isEqualTo(mapOf(
                Positions.create(2, 1) to Tiles.empty(),
                Positions.create(0, 0) to OLD_TILE,
                Positions.create(1, 1) to OLD_TILE))
    }

    companion object {

        private val OLD_TILE = Tiles.newBuilder()
                .withBackgroundColor(ANSITileColor.BLACK)
                .withForegroundColor(ANSITileColor.RED)
                .withCharacter('x')
                .buildCharacterTile()

        private val NEW_TILE = Tiles.newBuilder()
                .withBackgroundColor(ANSITileColor.BLACK)
                .withForegroundColor(ANSITileColor.RED)
                .withCharacter('y')
                .buildCharacterTile()

        val OLD_SNAPSHOT = DrawingSnapshot(
                selectedTile = OLD_TILE,
                selectedLayerIdx = 0,
                layers = listOf(
                        DrawLayer(
                                name = "Layer 0",
                                isSelected = true,
                                tiles = mapOf(
                                        Positions.create(0, 0) to OLD_TILE,
                                        Positions.create(1, 1) to OLD_TILE,
                                        Positions.create(2, 2) to OLD_TILE))))

        val NEW_SNAPSHOT = OLD_SNAPSHOT.copy(
                layers = listOf(OLD_SNAPSHOT.selectedLayer.copy(
                        tiles = mapOf(
                                Positions.create(2, 1) to NEW_TILE,
                                Positions.create(1, 1) to NEW_TILE,
                                Positions.create(2, 2) to OLD_TILE))))
    }
}
