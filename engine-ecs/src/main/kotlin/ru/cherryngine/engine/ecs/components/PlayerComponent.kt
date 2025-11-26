package ru.cherryngine.engine.ecs.components

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.ecs.EcsComponent
import java.util.*

data class PlayerComponent(
    var uuid: UUID,
    var viewContextIDs: Set<String>,
) : EcsComponent<PlayerComponent> {
    override fun type() = PlayerComponent

    companion object : ComponentType<PlayerComponent>()
}