package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class WorldComponent(
    var worldName: String,
) : Component<WorldComponent> {
    override fun type() = WorldComponent

    companion object : ComponentType<WorldComponent>()
}