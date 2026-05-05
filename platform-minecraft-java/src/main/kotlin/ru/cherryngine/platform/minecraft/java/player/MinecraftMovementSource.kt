package ru.cherryngine.platform.minecraft.java.player

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.player.MovementSnapshot
import ru.cherryngine.engine.core.player.MovementSource
import ru.cherryngine.engine.core.player.Player

@InstanceSingleton(platform = "minecraft")
class MinecraftMovementSource : MovementSource<MinecraftPlayer> {
    override fun canHandle(target: Player): Boolean = target is MinecraftPlayer

    override fun pollMovement(player: MinecraftPlayer): MovementSnapshot =
        MovementSnapshot(player.clientPosition, player.clientYawPitch, player.clientMovePlayerFlags.isOnGround)
}
