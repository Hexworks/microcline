package org.hexworks.microcline.views

import org.hexworks.zircon.api.input.Input


// TODO: let's use this: https://github.com/Hexworks/caves-of-zircon/blob/master/src/main/kotlin/org/hexworks/cavesofzircon/view/View.kt
// TODO: with this base class instead: https://github.com/Hexworks/caves-of-zircon/blob/master/src/main/kotlin/org/hexworks/cavesofzircon/view/BaseView.kt
interface View {

    fun display()

    fun respondToUserInput(input: Input): View
}
