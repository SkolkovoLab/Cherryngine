package ru.cherryngine.engine.ecs.components

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.ecs.EcsComponent

data class ViewableComponent(
    var visibleInContextIds: Set<String>,
) : EcsComponent<ViewableComponent> {
    override fun type() = ViewableComponent

    companion object : ComponentType<ViewableComponent>()
}