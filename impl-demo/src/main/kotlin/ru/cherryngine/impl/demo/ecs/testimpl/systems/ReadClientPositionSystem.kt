package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.DemoPacketHandler
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent

class ReadClientPositionSystem(
    val demoPacketHandler: DemoPacketHandler,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val player = demoPacketHandler.players[playerComponent.uuid] ?: return

        entity.configure {
            val positionComponent = it.getOrAdd(PositionComponent, ::PositionComponent)
            positionComponent.position = player.clientPosition
            positionComponent.yawPitch = player.clientYawPitch
        }
    }
}