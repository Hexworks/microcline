package org.hexworks.microcline.fragments

import org.hexworks.microcline.context.EditorContext
import org.hexworks.microcline.data.DrawLayer
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.data.Position


class LayerEditor(position: Position,
                  val layer: DrawLayer,
                  private val context: EditorContext) : Fragment {

    override val root = Components.panel()
            .withPosition(position)
            .withSize(50, 1)
            .build().apply {
                addFragment(LabelFragment(Position.create(0, 0), 10, layer))
                addFragment(SelectFragment(Position.create(9, 0), layer))
                addFragment(VisibleFragment(Position.create(16, 0), layer, context.drawLayerEditor))
                addFragment(LockFragment(Position.create(23, 0), layer))
                addFragment(MoveFragment(Position.create(29, 0), layer, context.drawLayerEditor))
                addFragment(ClearFragment(Position.create(33, 0), layer, context.drawLayerEditor))
                addFragment(RemoveFragment(Position.create(41, 0), layer, context.drawLayerEditor))
            }
}
