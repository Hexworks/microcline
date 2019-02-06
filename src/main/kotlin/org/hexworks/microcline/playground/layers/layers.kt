package org.hexworks.microcline.playground.layers

import org.hexworks.microcline.layers.LayerRegistry

object Layers {

    @JvmStatic
    fun main(args: Array<String>) {
        val r = LayerRegistry()

        val l1 = r.create()
        val l2 = r.create()
        val l3 = r.create()
        println("$l1, $l2, $l3")
        println("1. layers: ${r.visibleLayers()}")

        r.moveUp(l1)
        println("2. layers: ${r.visibleLayers()}")

        r.moveUp(l1)
        println("3. layers: ${r.visibleLayers()}")

        r.moveUp(l1)
        println("4. layers: ${r.visibleLayers()}")

        r.moveDown(l1)
        println("5. layers: ${r.visibleLayers()}")

        r.moveDown(l1)
        println("6. layers: ${r.visibleLayers()}")

        r.moveDown(l1)
        println("7. layers: ${r.visibleLayers()}")

    }

}
