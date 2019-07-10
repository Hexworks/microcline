package org.hexworks.microcline.dialogs

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.microcline.data.DrawMode
import org.hexworks.microcline.dialogs.base.OkCancelDialog
import org.hexworks.microcline.dialogs.result.MaybeModalResult
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.extensions.onSelection
import org.hexworks.zircon.api.extensions.processComponentEvents
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.ComponentEventType


class ModeSelectorDialog(
        screen: Screen,
        private var currentMode: DrawMode) : OkCancelDialog<DrawMode>(screen, CONTENT_SIZE, "Pick Draw Mode") {

    override val content = Components.radioButtonGroup()
            .withSize(CONTENT_SIZE)
            .build().apply {
                onSelection { selected ->
                    currentMode = DrawMode.valueOf(selected.key)
                }
                DrawMode.values().map { mode ->
                    addOption(mode.name, mode.label).apply {
                        if (currentMode == mode) {
                            isSelected = true
                        }
                    }
                }
            }

    init {
        okButton.processComponentEvents(ComponentEventType.ACTIVATED) {
            root.close(MaybeModalResult(Maybe.of(currentMode)))
        }
    }

    companion object {

        private val TOOL_COUNT = DrawMode.values().size
        val CONTENT_SIZE = Sizes.create(18, TOOL_COUNT)
    }
}
