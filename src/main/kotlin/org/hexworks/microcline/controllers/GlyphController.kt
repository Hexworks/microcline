package org.hexworks.microcline.controllers

import org.hexworks.microcline.common.MouseButton
import org.hexworks.microcline.components.GLYPH_P_SIZE_X
import org.hexworks.microcline.components.GLYPH_P_SIZE_Y
import org.hexworks.microcline.components.GlyphPanel
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.listener.MouseListener


class GlyphController(
        private val glyphPanel: GlyphPanel
) : MouseListener {

    override fun mousePressed(action: MouseAction) {
        // Only the left mouse button can be used
        if (action.button != MouseButton.LEFT.id) {
            return
        }

        val mPos = action.position
        val pos = glyphPanel.position

        // Ignore if clicked on the border
        // TODO: must be a better way... possible zircon feature?
        if ((mPos.x <= pos.x) || (mPos.x > pos.x + GLYPH_P_SIZE_X) ||
                (mPos.y <= pos.y) || (mPos.y > pos.y + GLYPH_P_SIZE_Y)) {
            return
        }

        glyphPanel.select(mPos)
    }

}
