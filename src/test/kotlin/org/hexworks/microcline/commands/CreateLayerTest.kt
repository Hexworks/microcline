package org.hexworks.microcline.commands

import org.assertj.core.api.Assertions.assertThat
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.microcline.data.DrawingSnapshot
import org.hexworks.zircon.api.Tiles
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a create layer command")
class CreateLayerTest {

    lateinit var snapshot: DrawingSnapshot

    @BeforeEach
    fun setUp() {
        snapshot = DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 0,
                layers = listOf(
                        DrawLayer("Layer 0")))
    }

    @DisplayName("When executing it it should properly create a new layer")
    @Test
    fun shouldProperlyAddNewLayer() {

        val result = CreateLayer().execute(snapshot)

        assertThat(result).isEqualTo(DrawingSnapshot(
                selectedTile = Tiles.defaultTile(),
                selectedLayerIdx = 0,
                layers = listOf(
                        DrawLayer("Layer 0"),
                        DrawLayer("Layer 1"))))
    }
}
