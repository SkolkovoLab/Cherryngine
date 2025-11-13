package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.DemoPacketHandler
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent

class WriteClientPositionSystem(
    val demoPacketHandler: DemoPacketHandler,
) : IteratingSystem(
    family { all(PlayerComponent, PositionComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val positionComponent = entity[PositionComponent]
        val player = demoPacketHandler.players[playerComponent.uuid] ?: return

        if (positionComponent.position != player.clientPosition || positionComponent.yawPitch != player.clientYawPitch) {
            player.teleport(positionComponent.position, positionComponent.yawPitch)
        }
    }
}