package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class ApartComponent(
    var apartName: String,
) : Component<ApartComponent> {
    override fun type() = ApartComponent

    companion object : ComponentType<ApartComponent>()
}