package org.hexworks.microcline

import org.hexworks.microcline.config.NewConfig
import org.hexworks.microcline.views.NewDrawView
import org.hexworks.zircon.api.SwingApplications


fun main(args: Array<String>) {
    NewDrawView(SwingApplications.startTileGrid(NewConfig.buildAppConfig())).dock()
}
