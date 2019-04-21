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
            .withSize(22, 1)
            .build().apply {
                addFragment(SelectFragment(Position.create(1, 0), layer))
                addFragment(VisibilityFragment(Position.create(2, 0), layer))
                addFragment(LabelFragment(Position.create(4, 0), 10, layer))
                addFragment(MoveFragment(Position.create(15, 0), layer, context.drawLayerEditor))
                addFragment(LockFragment(Position.create(18, 0), layer))
                addFragment(ClearFragment(Position.create(19, 0), layer))
                addFragment(RemoveFragment(Position.create(20, 0), layer, context.drawLayerEditor))
            }
}
