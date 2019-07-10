package org.hexworks.microcline.dialogs.result

import org.hexworks.zircon.api.component.modal.ModalResult

sealed class YesNoResult(val result: Boolean) : ModalResult

object Yes : YesNoResult(true)

object No : YesNoResult(false)
