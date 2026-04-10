package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.PlayerInputProvider
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.ecs.events.LastPlayerPositionEvent

class ReadClientPositionSystem(
    val inputProvider: PlayerInputProvider,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val uuid = entity[PlayerComponent].uuid
        val pos = inputProvider.getPosition(uuid) ?: return
        val yawPitch = inputProvider.getYawPitch(uuid) ?: return

        entity.configure {
            val posComp = it.getOrAdd(PositionComponent, ::PositionComponent)
            posComp.position = pos
            posComp.yawPitch = yawPitch
            it += LastPlayerPositionEvent(pos, yawPitch)
        }
    }
}
