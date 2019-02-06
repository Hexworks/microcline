package org.hexworks.microcline.playground.bug

import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.mvc.base.BaseView


object Bug {

    @JvmStatic
    fun main(args: Array<String>) {

        val app = SwingApplications.startApplication(AppConfigs.newConfig()
                .enableBetaFeatures()
                .build())

        app.dock(Bug.View())
    }

    class View : BaseView() {
        override val theme = ColorThemes.arc()

        override fun onDock() {
            val panel = Components.panel()
                    .withSize(50, 20)
                    .withPosition(5, 5)
                    .wrapWithBox(true)
                    .build().apply {
                        addComponent(Components.toggleButton()
                                .withText("whatever")
                                .withIsSelected(true)
                                .build()
                        )
                    }

            screen.addComponent(panel)
        }
    }

}
