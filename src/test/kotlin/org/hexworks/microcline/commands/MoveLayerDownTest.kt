package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a move layer down command")
class MoveLayerDownTest {

    lateinit var snapshot: DrawingSnapshot

    @BeforeEach
    fun setUp() {
        snapshot = DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0", tiles = mapOf(Positions.create(1, 1) to Tiles.defaultTile())),
                        DrawLayer("Layer 1", tiles = mapOf(Positions.create(2, 1) to Tiles.defaultTile()))))
    }

    @DisplayName("When executing it within bounds it should properly move the layer down")
    @Test
    fun shouldProperlySelectWhenWithinBounds() {

        val result = MoveLayerDown(0).execute(snapshot)

        assertThat(result).isEqualTo(DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 1", tiles = mapOf(Positions.create(2, 1) to Tiles.defaultTile())),
                        DrawLayer("Layer 0", tiles = mapOf(Positions.create(1, 1) to Tiles.defaultTile())))))
    }

    @DisplayName("When executing it below bounds it should return old state")
    @Test
    fun shouldNotMoveBelowBound() {

        val result = MoveLayerDown(-1).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it above bounds it should return old state")
    @Test
    fun shouldNotMoveAboveBound() {

        val result = MoveLayerDown(2).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it when layer is already at bottom it should return old state")
    @Test
    fun shouldNotMoveAtTopBound() {

        val result = MoveLayerDown(1).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }
}
