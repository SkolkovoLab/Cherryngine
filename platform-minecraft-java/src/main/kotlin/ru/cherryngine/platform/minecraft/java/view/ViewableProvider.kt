package ru.cherryngine.platform.minecraft.java.view

interface ViewableProvider {
    val viewables: Set<Viewable>

    data class Static(
        override val viewables: Set<Viewable>,
    ) : ViewableProvider
}

