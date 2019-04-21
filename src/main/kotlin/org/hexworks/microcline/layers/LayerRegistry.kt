package org.hexworks.microcline.layers

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.components.LayerHandler
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.events.LayerMovedDown
import org.hexworks.microcline.data.events.LayerMovedUp
import org.hexworks.microcline.data.events.LayerOrderChanged
import org.hexworks.microcline.data.events.LayerSelected
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.internal.Zircon
import java.util.*


class LayerRegistry(private val context: EditorContext) {

    private val order = LinkedList<Layer>()
    private var lastID = 0
    var selected = Maybe.empty<Layer>()

    init {
        Zircon.eventBus.subscribe<LayerMovedUp> {
            moveUp(it.layer)
        }
        Zircon.eventBus.subscribe<LayerMovedDown> {
            moveDown(it.layer)
        }
    }

    fun create() = Layer("Layer $lastID").also {
            lastID++
            order.add(0, it)
            Zircon.eventBus.publish(LayerOrderChanged(false))
            select(it)
        }

    fun remove(layer: Layer) {
        // The last layer cannot be removed.
        if (order.size == 1) {
            return
        }
        order.remove(layer)
        selected.ifPresent {
            // If we removed the selected layer, select the first on the list.
            if (it == layer) {
                select(order.first())
            }
        }
        Zircon.eventBus.publish(LayerOrderChanged(false))
    }

    fun moveUp(layer: Layer) {
        if (layer == order.first()) {
            return
        }

        val idx = order.indexOf(layer)
        order.removeAt(idx)
        order.add(idx-1, layer)
        Zircon.eventBus.publish(LayerOrderChanged(false))
    }

    fun moveDown(layer: Layer) {
        if (layer == order.last()) {
            return
        }

        val idx = order.indexOf(layer)
        order.removeAt(idx)
        order.add(idx+1, layer)
        Zircon.eventBus.publish(LayerOrderChanged(false))
    }

    fun layerHandlers(): List<LayerHandler> {
        val handlers = mutableListOf<LayerHandler>()
        order.forEachIndexed { i, l ->
            handlers.add(i, LayerHandler(
                    Position.create(0, i),
                    l == order.first(),
                    l == order.last(),
                    l,
                    context)
            )
        }
        return handlers
    }

    fun select(layer: Layer) {
        order.forEach { it.selectedProperty.value = false }
        layer.selectedProperty.value = true
        selected = Maybe.of(layer)
        Zircon.eventBus.publish(LayerSelected(layer))
    }

    fun layers(): List<Layer> {
        return order.toList()
    }

    fun visibleLayers(): List<Layer> {
        return order.filter { it.visibleProperty.value }
    }

}
