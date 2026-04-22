package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.player.PlayerPositionSource
import ru.cherryngine.engine.core.player.PositionSnapshot
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

@InstanceSingleton
class EcsPlayerPositionSource(
    private val ecsWorld: EcsWorld,
) : PlayerPositionSource {
    override fun canHandle(player: Player): Boolean {
        val entity = ecsWorld.getPlayerEntityOrNull(player.uuid) ?: return false
        return with(ecsWorld) { entity has PositionComponent }
    }

    override fun getDesired(player: Player): PositionSnapshot? {
        val entity = ecsWorld.getPlayerEntityOrNull(player.uuid) ?: return null
        val comp = with(ecsWorld) { entity.getOrNull(PositionComponent) } ?: return null
        return PositionSnapshot(comp.position, comp.yawPitch)
    }

    override fun acceptClientMovement(player: Player, position: Vec3D, yawPitch: YawPitch) {
        val entity = ecsWorld.getPlayerEntityOrNull(player.uuid) ?: return
        val comp = with(ecsWorld) { entity.getOrNull(PositionComponent) } ?: return
        comp.position = position
        comp.yawPitch = yawPitch
    }
}
