package org.hexworks.microcline

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.views.DrawView
import org.hexworks.zircon.api.SwingApplications


fun main(args: Array<String>) {
    val app = SwingApplications.startApplication(Config.buildAppConfig())

    app.dock(DrawView())
}
