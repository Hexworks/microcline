package org.hexworks.microcline.components

import org.hexworks.microcline.components.layerfragments.*
import org.hexworks.microcline.layers.Layer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.data.Position


class LayerHandler(position: Position, isFirst: Boolean, isLast: Boolean, val layer: Layer) {

    val panel = Components.panel()
            .withPosition(position)
            .withSize(22, 1)
            .build().apply {
                addFragment(SelectFragment(Position.create(1, 0), layer))
                addFragment(VisibilityFragment(Position.create(2, 0), layer.visibleProperty))
                addFragment(LabelFragment(Position.create(4, 0), 10, layer.labelProperty))
                addFragment(MoveFragment(Position.create(15, 0), isFirst, isLast, layer))
                addFragment(LockFragment(Position.create(18, 0), layer.lockedProperty))
                addFragment(ClearFragment(Position.create(19, 0), layer.lockedProperty, layer.layer))
                addFragment(RemoveFragment(Position.create(20, 0), layer))
            }

}
