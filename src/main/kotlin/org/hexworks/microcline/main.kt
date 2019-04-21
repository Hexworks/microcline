package org.hexworks.microcline

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.views.DrawView
import org.hexworks.zircon.api.SwingApplications

fun main() {

    val context = EditorContext()

    SwingApplications
            .startApplication(Config.buildAppConfig())
            .dock(DrawView(context))
}
