package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.player.PlayerOutputProvider
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.ecs.events.LastPlayerPositionEvent

class WriteClientPositionSystem(
    val outputProvider: PlayerOutputProvider,
) : IteratingSystem(
    family { all(PlayerComponent, PositionComponent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val uuid = entity[PlayerComponent].uuid
        val pos = entity[PositionComponent]
        val last = entity.getOrNull(LastPlayerPositionEvent)

        if (last == null || pos.position != last.position || pos.yawPitch != last.yawPitch) {
            outputProvider.teleport(uuid, pos.position, pos.yawPitch)
        }
    }
}
