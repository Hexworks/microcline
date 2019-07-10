package org.hexworks.microcline.model

import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.microcline.data.DrawLayer

class DrawLayerModel(drawLayer: DrawLayer,
                     index: Int) {

    val indexProperty = createPropertyFrom(index)
    val nameProperty = createPropertyFrom(drawLayer.name)
    val selectedProperty = createPropertyFrom(drawLayer.isSelected)
    val visibleProperty = createPropertyFrom(drawLayer.isVisible)
    val lockedProperty = createPropertyFrom(drawLayer.isLocked)

    var index: Int by indexProperty.asDelegate()
    var name: String by nameProperty.asDelegate()
    var isSelected: Boolean by selectedProperty.asDelegate()
    var isVisible: Boolean by visibleProperty.asDelegate()
    var isLocked: Boolean by lockedProperty.asDelegate()


}
