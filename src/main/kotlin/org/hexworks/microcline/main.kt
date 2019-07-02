package org.hexworks.microcline

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.controllers.DrawController
import org.hexworks.microcline.views.DrawView
import org.hexworks.zircon.api.SwingApplications

fun main() {
    val app = SwingApplications
            .startApplication(Config.buildAppConfig())
    val context = EditorContext()
    DrawController(context = context)
    app.dock(DrawView(context))
}
