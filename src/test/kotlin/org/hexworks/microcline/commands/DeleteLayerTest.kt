package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a delete layer command")
class DeleteLayerTest {

    lateinit var snapshot: DrawingSnapshot

    @BeforeEach
    fun setUp() {
        snapshot = DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0", tiles = mapOf(Positions.create(1, 1) to Tiles.defaultTile())),
                        DrawLayer("Layer 1", tiles = mapOf(Positions.create(2, 2) to Tiles.defaultTile()))))
    }

    @DisplayName("When executing it within layer bounds it should properly delete the layer")
    @Test
    fun shouldProperlyDeleteLayer() {

        val result = DeleteLayer(0).execute(snapshot)

        assertThat(result).isEqualTo(DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 0,
                layers = listOf(
                        DrawLayer("Layer 1", tiles = mapOf(Positions.create(2, 2) to Tiles.defaultTile())))))
    }

    @DisplayName("When executing it with layer index below 0 it should return old state")
    @Test
    fun shouldNotDeleteWhenBelowBound() {

        val result = DeleteLayer(-1).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it with layer index above last index it should return old state")
    @Test
    fun shouldNotDeleteWhenAboveBound() {

        val result = DeleteLayer(2).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it with selected layer it should return old state")
    @Test
    fun shouldNotDeleteWhenSelected() {

        val result = DeleteLayer(1).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }
}
