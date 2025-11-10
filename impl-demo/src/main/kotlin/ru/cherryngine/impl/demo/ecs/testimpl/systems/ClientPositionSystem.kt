package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameObject
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerPosRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerRotPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundMovePlayerStatusOnlyPacket
import ru.cherryngine.lib.minecraft.protocol.types.MovePlayerFlags

class ClientPositionSystem(
    val gameScene: GameScene,
) : GameSystem{
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val gameObjects = gameScene.objectsWithComponent(PlayerComponent::class)
        gameObjects.forEach { gameObject ->
            val playerComponent = gameObject[PlayerComponent::class]!!
            playerComponent.packets.forEach { packet ->
                when (packet) {
                    is ServerboundMovePlayerPosPacket -> onMove(gameObject, packet.pos, null, packet.flags)
                    is ServerboundMovePlayerPosRotPacket -> onMove(gameObject, packet.pos, packet.yawPitch, packet.flags)
                    is ServerboundMovePlayerRotPacket -> onMove(gameObject, null, packet.yawPitch, packet.flags)
                    is ServerboundMovePlayerStatusOnlyPacket -> onMove(gameObject, null, null, packet.flags)
                }
            }
        }
    }

    fun onMove(
        gameObject: GameObject,
        pos: Vec3D?,
        yawPitch: YawPitch?,
        flags: MovePlayerFlags,
    ) {
        val oldPosition = gameObject[ClientPositionComponent::class]
        val position = pos ?: oldPosition?.clientPosition ?: Vec3D.ZERO
        val yawPitch = yawPitch ?: oldPosition?.clientYawPitch ?: YawPitch.ZERO
        gameObject[ClientPositionComponent::class] = ClientPositionComponent(position, yawPitch)
    }
}