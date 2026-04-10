package ru.cherryngine.engine.minecraft.view

interface ViewableProvider {
    val viewables: Set<Viewable>

    data class Static(
        override val viewables: Set<Viewable>,
    ) : ViewableProvider
}

