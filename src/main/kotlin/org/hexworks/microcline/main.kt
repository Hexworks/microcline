package org.hexworks.microcline

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.views.DrawView
import org.hexworks.zircon.api.SwingApplications

fun main() {
    SwingApplications
            .startApplication(Config.buildAppConfig())
            .dock(DrawView())
}
