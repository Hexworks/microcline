package org.hexworks.microcline.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Given a MutableList")
class IterableExtensionsTest {

    @DisplayName("When removing a range it should work properly")
    @Test
    fun shouldRemoveRangeProperly() {
        val list = mutableListOf(1, 2, 3, 4)

        list.removeRange(1..2)

        assertThat(list).containsExactly(1, 4)
    }

}
