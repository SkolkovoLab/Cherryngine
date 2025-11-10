package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.lib.minecraft.server.Connection

data class PlayerComponent(
    var connection: Connection,
    var viewContextID: String,
) : Component<PlayerComponent> {
    override fun type() = PlayerComponent

    companion object : ComponentType<PlayerComponent>()
}