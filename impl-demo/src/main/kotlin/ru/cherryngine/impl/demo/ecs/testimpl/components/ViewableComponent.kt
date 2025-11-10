package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class ViewableComponent(
    var viewContextID: String,
) : Component<ViewableComponent> {
    override fun type() = ViewableComponent

    companion object : ComponentType<ViewableComponent>()
}