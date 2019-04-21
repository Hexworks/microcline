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
    val view = DrawView(context)
    DrawController(
            context = context,
            view = view)

    app.dock(view)
}
