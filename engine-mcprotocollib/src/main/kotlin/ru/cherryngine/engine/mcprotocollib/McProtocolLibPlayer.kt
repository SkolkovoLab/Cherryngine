package ru.cherryngine.engine.mcprotocollib

import net.kyori.adventure.text.Component
import org.geysermc.mcprotocollib.auth.GameProfile
import org.geysermc.mcprotocollib.network.Session
import org.geysermc.mcprotocollib.protocol.MinecraftConstants
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.world.ImmutableLayerKey
import java.util.*

class McProtocolLibPlayer(
    val session: Session,
) : Player {
    private val profile: GameProfile
        get() = session.getFlag(MinecraftConstants.PROFILE_KEY)

    override val uuid: UUID
        get() = profile.id
    override val username: String
        get() = profile.name

    var clientPosition: Vec3D = Vec3D.ZERO
    var clientYawPitch: YawPitch = YawPitch.ZERO

    var sentChunkCacheCenter: ChunkPos? = null
    val sentChunks: MutableSet<ChunkPos> = mutableSetOf()
    var sentChunksBase: ImmutableLayerKey? = null
    val currentVisibleEntities: MutableSet<McProtocolLibEntity> = hashSetOf()
    val chunksToRefresh: MutableSet<ChunkPos> = hashSetOf()

    override fun sendMessage(message: Component) {
        session.send(ClientboundSystemChatPacket(message, false))
    }
}
