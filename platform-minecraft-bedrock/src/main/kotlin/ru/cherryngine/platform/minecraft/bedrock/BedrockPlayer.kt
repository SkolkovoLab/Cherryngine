package ru.cherryngine.platform.minecraft.bedrock

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.BedrockServerSession
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket
import org.cloudburstmc.protocol.bedrock.packet.TextPacket
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.bedrock.utils.cloudburstVector3f
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

class BedrockPlayer(
    val session: BedrockServerSession,
    override val uuid: UUID,
    override val username: String,
) : Player {
    override var clientPosition: Vec3D = Vec3D.ZERO
    override var clientYawPitch: YawPitch = YawPitch.ZERO
    val runtimeEntityId: Long = uuid.leastSignificantBits and Long.MAX_VALUE
    val pendingCommands: ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()

    var heldItemSlot: Int = 0
    val pendingSlotDeltas: ConcurrentLinkedQueue<Int> = ConcurrentLinkedQueue()
    val pendingSwings: AtomicInteger = AtomicInteger(0)
    val pendingUseItems: AtomicInteger = AtomicInteger(0)
    var prevMissedSwing: Boolean = false
    var prevStartUsingItem: Boolean = false

    val sentChunks: MutableSet<Long> = mutableSetOf()
    var sentChunkCacheCenter: Long = Long.MIN_VALUE
    val visibleEntities: MutableSet<ru.cherryngine.platform.minecraft.bedrock.entity.BedrockEntity> = mutableSetOf()

    override var viewContextIDs: Set<String> = emptySet()

    override fun teleport(position: Vec3D, yawPitch: YawPitch) {
        clientPosition = position
        clientYawPitch = yawPitch
        val packet = MovePlayerPacket()
        packet.runtimeEntityId = runtimeEntityId
        packet.position = position.plus(0.0, 1.62, 0.0).cloudburstVector3f()
        packet.rotation = Vector3f.from(
            yawPitch.pitch,
            yawPitch.yaw,
            yawPitch.yaw
        )
        packet.mode = MovePlayerPacket.Mode.TELEPORT
        packet.teleportationCause = MovePlayerPacket.TeleportationCause.COMMAND
        session.sendPacket(packet)
    }

    override fun correctClientPosition(position: Vec3D) {
        // Bedrock MovePlayerPacket не имеет relative-флагов — эмулируем Java-аналог
        // через absolute teleport с сохранённым clientYawPitch: камера визуально не поворачивается.
        clientPosition = position
        val packet = MovePlayerPacket()
        packet.runtimeEntityId = runtimeEntityId
        packet.position = position.plus(0.0, 1.62, 0.0).cloudburstVector3f()
        packet.rotation = Vector3f.from(
            clientYawPitch.pitch,
            clientYawPitch.yaw,
            clientYawPitch.yaw
        )
        packet.mode = MovePlayerPacket.Mode.TELEPORT
        packet.teleportationCause = MovePlayerPacket.TeleportationCause.COMMAND
        session.sendPacket(packet)
    }

    override fun setVelocity(velocity: Vec3D) {
        val packet = SetEntityMotionPacket()
        packet.runtimeEntityId = runtimeEntityId
        packet.motion = (velocity / 20.0).cloudburstVector3f()
        session.sendPacket(packet)
    }

    override fun sendMessage(message: Component) {
        val packet = TextPacket().apply {
            type = TextPacket.Type.RAW
            setMessage(PlainTextComponentSerializer.plainText().serialize(message))
            setNeedsTranslation(false)
            xuid = ""
            sourceName = ""
        }
        session.sendPacket(packet)
    }
}
