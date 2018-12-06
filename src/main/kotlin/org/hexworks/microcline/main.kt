package org.hexworks.microcline

import org.hexworks.microcline.config.Config
import org.hexworks.microcline.views.DrawView
import org.hexworks.zircon.api.SwingApplications


fun main(args: Array<String>) {
    DrawView(SwingApplications.startTileGrid(Config.buildAppConfig())).dock()
}
