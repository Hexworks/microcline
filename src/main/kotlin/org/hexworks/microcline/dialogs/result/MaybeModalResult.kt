package org.hexworks.microcline.dialogs.result

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.component.modal.ModalResult

data class MaybeModalResult<T : Any>(
        val result: Maybe<T> = Maybe.empty()) : ModalResult
