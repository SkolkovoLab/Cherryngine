package ru.cherryngine.platform.minecraft.java.player

import net.kyori.adventure.text.Component
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.packet.server.play.SystemChatPacket
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.platform.minecraft.java.network.Connection
import ru.cherryngine.platform.minecraft.java.utils.ChunkUtils
import ru.cherryngine.platform.minecraft.java.view.BlocksViewable
import ru.cherryngine.platform.minecraft.java.world.ChunkPos
import ru.cherryngine.platform.minecraft.java.world.ImmutableLayerKey
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.random.Random

class MinecraftPlayer(
    val connection: Connection,
) : Player {
    override val uuid get() = connection.gameProfile.uuid
    override val username get() = connection.gameProfile.name()

    /**
     * Minecraft-side entity ID для этого игрока. Нужен для пакетов, которые
     * адресуют игрока как entity (SetPassengersPacket и т.п.). Range [1, 1_000_000)
     * — disjoint с серверными [McEntity] ID ([1_000_000, 9_000_000)).
     */
    val entityId: Int = Random.nextInt(1, 1_000_000)

    // Пишется с сетевого потока на каждый входящий игровой пакет.
    private val incomingPackets = ConcurrentLinkedQueue<ClientPacket>()

    // Читается только с игрового потока. Перекладывается из incomingPackets через snapshotPackets()
    // в начале INPUT-стадии тика. Все Source'ы читают именно отсюда.
    var tickPackets: List<ClientPacket> = emptyList()
        private set

    val currentVisibleBlocksViewables: MutableList<BlocksViewable> = mutableListOf()
    val chunksToRefresh: MutableSet<ChunkPos> = hashSetOf()

    var sentChunksBase: ImmutableLayerKey? = null
    var sentChunkCacheCenter: ChunkPos? = null
    val sentChunks: MutableSet<ChunkPos> = mutableSetOf()

    override var viewContextIDs: Set<String> = emptySet()

    fun enqueuePacket(packet: ClientPacket) {
        incomingPackets.add(packet)
    }

    /** Атомарно перекладывает входящую очередь в tickPackets. Вызывается раз за тик в INPUT. */
    fun snapshotPackets() {
        val snapshot = mutableListOf<ClientPacket>()
        while (true) snapshot.add(incomingPackets.poll() ?: break)
        tickPackets = snapshot
    }

    inline fun <reified P : ClientPacket> packets(): List<P> =
        tickPackets.filterIsInstance<P>()

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

    override fun sendMessage(message: Component) {
        connection.sendPacket(SystemChatPacket(message, false))
    }
}
