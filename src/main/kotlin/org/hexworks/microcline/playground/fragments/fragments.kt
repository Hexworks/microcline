package org.hexworks.microcline.playground.fragments

import org.hexworks.cobalt.events.api.subscribe
import org.hexworks.microcline.data.events.LayerOrderChanged
import org.hexworks.microcline.layers.LayerRegistry
import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.mvc.base.BaseView
import org.hexworks.zircon.internal.Zircon


object FragmentsExample {

    @JvmStatic
    fun main(args: Array<String>) {

        val app = SwingApplications.startApplication(AppConfigs.newConfig()
                .enableBetaFeatures()
                .build())

        app.dock(View())
    }

    class View : BaseView() {

        override val theme = ColorThemes.amigaOs()

        override fun onDock() {
            val registry = LayerRegistry().apply {
                create()
                create()
                create()
            }

            val panel = Components.panel()
                    .withSize(50, 20)
                    .withPosition(5, 5)
                    .wrapWithBox(true)
                    .build().apply {
                        registry.layerHandlers().forEach {
                            addComponent(it.panel)
                        }

                        Zircon.eventBus.subscribe<LayerOrderChanged> { event ->
                            children.forEach { removeComponent(it) }
                            registry.layerHandlers().forEach {
                                addComponent(it.panel)
                            }
                            applyColorTheme(ColorThemes.amigaOs())
                        }
                    }

            screen.addComponent(panel)
        }
    }
}
