package org.hexworks.microcline.converter

import org.hexworks.cobalt.databinding.api.converter.IsomorphicConverter

object NegationConverter : IsomorphicConverter<Boolean, Boolean> {

    override fun convert(source: Boolean) = source.not()

    override fun convertBack(target: Boolean) = target.not()
}
