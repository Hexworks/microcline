package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Tiles
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a select layer command")
class SelectLayerTest {

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

    @DisplayName("When executing it within bounds it should properly select a new layer")
    @Test
    fun shouldProperlySelectWhenWithinBounds() {

        val result = SelectLayer(0).execute(snapshot)

        assertThat(result).isEqualTo(DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 0,
                layers = listOf(
                        DrawLayer("Layer 0", isSelected = true),
                        DrawLayer("Layer 1"))))
    }

    @DisplayName("When executing it below bounds it should not select a new layer")
    @Test
    fun shouldNotSelectBelowBound() {

        val result = SelectLayer(-1).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

    @DisplayName("When executing it above bounds it should not select a new layer")
    @Test
    fun shouldNotSelectAboveBound() {

        val result = SelectLayer(2).execute(snapshot)

        assertThat(result).isEqualTo(snapshot)
    }

}
