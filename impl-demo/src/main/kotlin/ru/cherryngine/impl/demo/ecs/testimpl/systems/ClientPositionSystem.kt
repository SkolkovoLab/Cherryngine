package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.EventsComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerStatusOnlyPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags

class ClientPositionSystem() : IteratingSystem(
    family { all(EventsComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val events = entity[EventsComponent]
        val packetsEvent = events[PacketsEvent::class] ?: return
        packetsEvent.packets.forEach { packet ->
            when (packet) {
                is ServerboundMovePlayerPosPacket -> onMove(entity, packet.pos, null, packet.flags)
                is ServerboundMovePlayerPosRotPacket -> onMove(
                    entity,
                    packet.pos,
                    packet.yawPitch,
                    packet.flags
                )

                is ServerboundMovePlayerRotPacket -> onMove(entity, null, packet.yawPitch, packet.flags)
                is ServerboundMovePlayerStatusOnlyPacket -> onMove(entity, null, null, packet.flags)
            }
        }
    }

    fun onMove(
        entity: Entity,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val positionComponent = entity.getOrNull(ClientPositionComponent)
        val position = pos ?: positionComponent?.clientPosition ?: Vec3D.ZERO
        val yawPitch = yawPitch ?: positionComponent?.clientYawPitch ?: YawPitch.ZERO
        if (positionComponent != null) {
            positionComponent.clientPosition = position
            positionComponent.clientYawPitch = yawPitch
        } else {
            entity.configure {
                it += ClientPositionComponent(position, yawPitch)
            }
        }
    }
}