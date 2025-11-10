package ru.cherryngine.impl.demo.view

interface ViewableProvider {
    val viewables: Set<Viewable>

    data class Static(
        override val viewables: Set<Viewable>,
    ) : ViewableProvider
}

