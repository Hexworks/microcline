package org.hexworks.microcline.data

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.extensions.replaceAt
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile

/**
 * Holds an immutable state of a [Drawing]. By default a [DrawingSnapshot]
 * will be created with an empty selected [DrawLayer].
 */
data class DrawingSnapshot(
        /**
         * The selected [Tile] which will be used for drawing.
         */
        val selectedTile: Tile,
        /**
         * The index of the [DrawLayer] which is currently selected.
         */
        val selectedLayerIdx: Int = 0,
        /**
         * The layers which this [Drawing] is composed of from
         * bottom to top (z axis).
         */
        val layers: List<DrawLayer> = listOf(DrawLayer(
                name = "Layer $selectedLayerIdx",
                isSelected = true))) {

    val selectedLayer: DrawLayer = layers[selectedLayerIdx]

    /**
     * Calculates the difference between this [DrawingSnapshot] and
     * [other], eg: tells what needs to be modified in [other] to
     * get to the state of **this** snapshot.
     */
    fun diff(other: DrawingSnapshot): Map<Position, Tile> {
        val diff = mutableMapOf<Position, Tile>()
        val prevTiles = other.selectedLayer.tiles
        val currTiles = selectedLayer.tiles

        // new tiles
        diff.putAll(currTiles.filter {
            prevTiles.containsKey(it.key).not()
        })
        // modified tiles
        diff.putAll(currTiles.filter {
            prevTiles.containsKey(it.key) && prevTiles[it.key] != it.value
        })
        // deleted tiles
        diff.putAll(prevTiles.filter {
            currTiles.containsKey(it.key).not()
        }.map {
            it.key to Tiles.empty()
        })
        return diff
    }

    fun indexOf(layer: DrawLayer) = layers.indexOf(layer)

    fun findLayerByName(name: String): Maybe<DrawLayer> {
        return Maybe.ofNullable(layers.firstOrNull {
            it.name == name
        })
    }

    fun withSelectedLayer(layer: DrawLayer): DrawingSnapshot {
        return copy(layers = layers.replaceAt(selectedLayerIdx, layer))
    }
}
