package org.hexworks.microcline.controllers

import org.hexworks.microcline.common.MouseButton
import org.hexworks.microcline.panels.PALETTE_P_SIZE_X
import org.hexworks.microcline.panels.PALETTE_P_SIZE_Y
import org.hexworks.microcline.panels.PalettePanel
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener


class PaletteController(
        private val palettePanel: PalettePanel
) : MouseListener {

    override fun mousePressed(action: MouseAction) {
        // TODO: `action` can be destructured like this:
        // TODO: val (actionType, button, position) = action
        // TODO: more here: https://kotlinlang.org/docs/reference/multi-declarations.html
        // Center mouse button is ignored
        if (action.button == MouseButton.CENTER) {
            return
        }

        val mPos = action.position
        val pos = palettePanel.position

        // Ignore if clicked on the border
        // TODO: must be a better way... possible zircon feature?
        // TODO: we can use buttons for this (we can have buttons now without decoration)
        if ((mPos.x <= pos.x) || (mPos.x > pos.x + PALETTE_P_SIZE_X) ||
                (mPos.y <= pos.y) || (mPos.y > pos.y + PALETTE_P_SIZE_Y)) {
            return
        }

        palettePanel.select(mPos.minus(pos), action.button)
    }

}
