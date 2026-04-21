package ru.cherryngine.platform.minecraft.java.player

import net.kyori.adventure.text.Component
import net.minestom.server.coordinate.Vec
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.server.play.PlayerPositionAndLookPacket
import net.minestom.server.network.packet.server.play.SystemChatPacket
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.java.network.Connection
import ru.cherryngine.platform.minecraft.java.utils.ChunkUtils
import ru.cherryngine.platform.minecraft.java.view.BlocksViewable
import ru.cherryngine.platform.minecraft.java.view.Viewable
import ru.cherryngine.platform.minecraft.java.world.ChunkPos
import ru.cherryngine.platform.minecraft.java.world.ImmutableLayerKey
import ru.cherryngine.platform.minecraft.java.world.MovePlayerFlags
import java.util.concurrent.ConcurrentLinkedQueue

class MinecraftPlayer(
    val connection: Connection,
) : Player {
    override val uuid get() = connection.gameProfile.uuid
    override val username get() = connection.gameProfile.name()

    var clientPosition: Vec3D = Vec3D.ZERO
    var clientYawPitch: YawPitch = YawPitch.ZERO
    var clientMovePlayerFlags: MovePlayerFlags = MovePlayerFlags(false, false)
    var isSneaking: Boolean = false
    val pendingCommands: ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()
    val pendingSuggestions: ConcurrentLinkedQueue<Pair<Int, String>> = ConcurrentLinkedQueue()

    val currentVisibleViewables: MutableSet<Viewable> = hashSetOf()
    val currentVisibleBlocksViewables: MutableList<BlocksViewable> = mutableListOf()
    val chunksToRefresh: MutableSet<ChunkPos> = hashSetOf()

    var sentChunksBase: ImmutableLayerKey? = null
    var sentChunkCacheCenter: ChunkPos? = null
    val sentChunks: MutableSet<ChunkPos> = mutableSetOf()

    var viewContextIDs: Set<String> = emptySet()

    fun getBlockId(pos: Vec3I): Int {
        val chunkPos = ChunkUtils.chunkPosFromVec3I(pos)
        val blockPos = Vec3I(
            ChunkUtils.globalToSectionRelative(pos.x),
            pos.y,
            ChunkUtils.globalToSectionRelative(pos.z)
        )
        val block = currentVisibleBlocksViewables.asReversed().firstNotNullOfOrNull {
            if (it.chunkPos != chunkPos) return@firstNotNullOfOrNull null
            it.getBlockId(blockPos)
        }
        return block ?: 0
    }

    fun getBlock(pos: Vec3I): Block {
        return Block.fromStateId(getBlockId(pos)) ?: Block.AIR
    }

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        clientPosition = position
        clientYawPitch = yawPitch

        connection.sendPacket(
            PlayerPositionAndLookPacket(
                0,
                Vec(position.x, position.y, position.z),
                Vec.ZERO,
                yawPitch.yaw.toFloat(),
                yawPitch.pitch.toFloat(),
                0
            )
        )
    }

    override fun sendMessage(message: Component) {
        connection.sendPacket(SystemChatPacket(message, false))
    }
}
