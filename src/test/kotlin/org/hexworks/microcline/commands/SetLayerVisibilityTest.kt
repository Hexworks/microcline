package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Tiles
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a change layer visibility command")
class SetLayerVisibilityTest {

    lateinit var snapshot: DrawingSnapshot

    @BeforeEach
    fun setUp() {
        snapshot = DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0"),
                        DrawLayer("Layer 1")))
    }

    @DisplayName("When executing it within layer bounds it should properly set the layer's visibility")
    @Test
    fun shouldProperlySetVisibilityWhenWithinBounds() {

        val result = SetLayerVisibility(0, false).execute(snapshot)

        assertThat(result).isEqualTo(DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 1,
                layers = listOf(
                        DrawLayer("Layer 0", isVisible = false),
                        DrawLayer("Layer 1"))))
    }

    @DisplayName("When executing it with layer index below 0 it should return old state")
    @Test
    fun shouldNotSelectBelowBound() {

        val result = SetLayerVisibility(-1, false).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it with layer index above last index it should return old state")
    @Test
    fun shouldNotSelectAboveBound() {

        val result = SetLayerVisibility(2, false).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }
}
