package ru.cherryngine.platform.minecraft.bedrock

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.cloudburstmc.protocol.bedrock.BedrockServerSession
import org.cloudburstmc.protocol.bedrock.packet.TextPacket
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class BedrockPlayer(
    val session: BedrockServerSession,
    override val uuid: UUID,
    override val username: String,
) : Player {
    var clientPosition: Vec3D = Vec3D.ZERO
    var clientYawPitch: YawPitch = YawPitch.ZERO
    val runtimeEntityId: Long = uuid.leastSignificantBits and Long.MAX_VALUE
    val pendingCommands: ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()
    val sentChunks: MutableSet<Long> = mutableSetOf()
    var sentChunkCacheCenter: Long = Long.MIN_VALUE
    val visibleEntities: MutableSet<ru.cherryngine.platform.minecraft.bedrock.entity.BedrockEntity> = mutableSetOf()

    var viewContextIDs: Set<String> = emptySet()

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
