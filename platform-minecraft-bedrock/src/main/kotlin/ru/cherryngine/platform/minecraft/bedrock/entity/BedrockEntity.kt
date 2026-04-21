package ru.cherryngine.platform.minecraft.bedrock.entity

import org.cloudburstmc.math.vector.Vector2f
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataMap
import org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket
import org.cloudburstmc.protocol.bedrock.packet.MoveEntityAbsolutePacket
import org.cloudburstmc.protocol.bedrock.packet.RemoveEntityPacket
import org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.bedrock.BedrockPlayer
import ru.cherryngine.platform.minecraft.bedrock.utils.cloudburstVector3f
import ru.cherryngine.platform.minecraft.java.world.ChunkPos
import java.util.concurrent.atomic.AtomicLong

class BedrockEntity(
    val runtimeEntityId: Long = NEXT_ID.getAndIncrement(),
    val identifier: String,
) {
    val metadata = EntityDataMap()
    var position: Vec3D = Vec3D.ZERO
    var yawPitch: YawPitch = YawPitch.ZERO
    val viewers = mutableSetOf<BedrockPlayer>()
    var viewerPredicate: (BedrockPlayer) -> Boolean = { true }
    var viewContextIDs: Set<String> = emptySet()

    val chunkPos: ChunkPos get() = ChunkPos(position.x.toInt() shr 4, position.z.toInt() shr 4)

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        this.position = position
        this.yawPitch = yawPitch
        val packet = MoveEntityAbsolutePacket()
        packet.runtimeEntityId = runtimeEntityId
        packet.position = position.cloudburstVector3f()
        packet.rotation = Vector3f.from(yawPitch.pitch, yawPitch.yaw, yawPitch.yaw)
        packet.isTeleported = true
        packet.isOnGround = false
        viewers.forEach { it.session.sendPacket(packet) }
    }

    fun resendMeta() {
        val packet = SetEntityDataPacket()
        packet.runtimeEntityId = runtimeEntityId
        packet.metadata.putAll(metadata)
        viewers.forEach { it.session.sendPacket(packet) }
    }

    fun show(player: BedrockPlayer) {
        if (!viewers.add(player)) return
        val spawn = AddEntityPacket()
        spawn.uniqueEntityId = runtimeEntityId
        spawn.runtimeEntityId = runtimeEntityId
        spawn.identifier = identifier
        spawn.position = position.cloudburstVector3f()
        spawn.motion = Vector3f.ZERO
        spawn.rotation = Vector2f.from(yawPitch.pitch, yawPitch.yaw)
        spawn.headRotation = yawPitch.yaw
        spawn.metadata.putAll(metadata)
        player.session.sendPacket(spawn)
    }

    fun hide(player: BedrockPlayer) {
        if (!viewers.remove(player)) return
        val packet = RemoveEntityPacket()
        packet.uniqueEntityId = runtimeEntityId
        player.session.sendPacket(packet)
    }

    companion object {
        private val NEXT_ID = AtomicLong(1000)
    }
}
