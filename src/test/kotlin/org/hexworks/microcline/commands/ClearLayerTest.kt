package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a clear layer command")
class ClearLayerTest {

    lateinit var snapshot: DrawingSnapshot

    @BeforeEach
    fun setUp() {
        snapshot = DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0", tiles = mapOf(Positions.create(1, 1) to Tiles.defaultTile())),
                        DrawLayer("Layer 1")))
    }

    @DisplayName("When executing it within layer bounds it should properly clear the layer")
    @Test
    fun shouldProperlyClearLayer() {

        val result = ClearLayer(0).execute(snapshot)

        assertThat(result).isEqualTo(DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0"),
                        DrawLayer("Layer 1"))))
    }

    @DisplayName("When executing it with layer index below 0 it should return old state")
    @Test
    fun shouldNotClearWhenBelowBound() {

        val result = ClearLayer(-1).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it with layer index above last index it should return old state")
    @Test
    fun shouldNotClearWhenAboveBound() {

        val result = ClearLayer(2).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }
}
