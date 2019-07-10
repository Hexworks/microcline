package org.hexworks.microcline.extensions

fun <E> Iterable<E>.replaceAt(index: Int, item: E): List<E> = mapIndexed { idx, element ->
    if (idx == index) item else element
}

fun <E> MutableList<E>.removeRange(range: IntRange) {
    range.reversed().forEach {
        this.removeAt(it)
    }
}
