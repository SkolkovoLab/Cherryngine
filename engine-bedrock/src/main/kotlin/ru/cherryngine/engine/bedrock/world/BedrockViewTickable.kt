package ru.cherryngine.engine.bedrock.world

import org.cloudburstmc.math.vector.Vector3i
import org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket
import org.cloudburstmc.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket
import ru.cherryngine.engine.bedrock.BedrockPlayer
import ru.cherryngine.engine.bedrock.BedrockWorldServiceHandler
import ru.cherryngine.engine.bedrock.entity.BedrockEntityRegistry
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.ServerWorld
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.world.LayeredWorld
import kotlin.time.Duration

@InstanceSingleton(platform = "bedrock", stage = TickStage.POST)
class BedrockViewTickable(
    private val playerManager: PlayerManager,
    private val worldServiceHandler: BedrockWorldServiceHandler,
    private val blockMapping: BedrockBlockMapping,
    private val serverWorld: ServerWorld,
    private val entityRegistry: BedrockEntityRegistry,
) : Tickable {

    companion object {
        const val RENDER_DISTANCE = 4
        const val ENTITY_RENDER_DISTANCE = 2
        const val MAX_CHUNKS_PER_TICK = 8
    }

    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val bp = player as? BedrockPlayer ?: continue
            val contextIDs = worldServiceHandler.getContextsForPlayer(player.uuid)
            val layers = serverWorld.getLayersForContexts(contextIDs)
            val dimensionType = serverWorld.dimensionType ?: continue

            if (layers.isEmpty()) continue

            val clientChunkPos = ChunkUtils.chunkPosFromVec3D(bp.clientPosition)
            val packedCenter = ChunkPos.pack(clientChunkPos.x, clientChunkPos.z)

            if (bp.sentChunkCacheCenter != packedCenter) {
                bp.sentChunkCacheCenter = packedCenter
                val publisher = NetworkChunkPublisherUpdatePacket()
                publisher.position = Vector3i.from(
                    clientChunkPos.x shl 4,
                    bp.clientPosition.y.toInt(),
                    clientChunkPos.z shl 4
                )
                publisher.radius = RENDER_DISTANCE shl 4
                bp.session.sendPacket(publisher)
            }

            // Chunks
            val world = LayeredWorld(dimensionType, layers)
            val chunks = ChunkUtils.getChunksInRange(clientChunkPos, RENDER_DISTANCE)
            val inRange = chunks.map { ChunkPos.pack(it.x, it.z) }.toSet()
            bp.sentChunks.retainAll(inRange)

            var sent = 0
            for (chunkPos in chunks) {
                if (sent >= MAX_CHUNKS_PER_TICK) break
                val packed = ChunkPos.pack(chunkPos.x, chunkPos.z)
                if (packed in bp.sentChunks) continue

                val chunkData = world.getChunkData(chunkPos)
                val (subChunksLength, dataBuf) = BedrockChunkSerializer.serialize(
                    chunkData.sections, blockMapping
                )

                val packet = LevelChunkPacket()
                packet.chunkX = chunkPos.x
                packet.chunkZ = chunkPos.z
                packet.dimension = 0
                packet.subChunksLength = subChunksLength
                packet.isCachingEnabled = false
                packet.isRequestSubChunks = false
                packet.data = dataBuf
                bp.session.sendPacket(packet)

                bp.sentChunks.add(packed)
                sent++
            }

            // Entities
            run {
                val entityChunks = ChunkUtils.getChunksInRange(clientChunkPos, ENTITY_RENDER_DISTANCE).toSet()
                val visibleEntities = entityRegistry.allEntities().filter { entity ->
                    entity.chunkPos in entityChunks &&
                    (entity.viewContextIDs.isEmpty() || entity.viewContextIDs.any { it in contextIDs }) &&
                    entity.viewerPredicate(bp)
                }.toSet()

                // Hide entities no longer visible
                bp.visibleEntities.removeAll { entity ->
                    val shouldHide = entity !in visibleEntities
                    if (shouldHide) entity.hide(bp)
                    shouldHide
                }

                // Show new entities
                for (entity in visibleEntities) {
                    if (entity !in bp.visibleEntities) {
                        entity.show(bp)
                        bp.visibleEntities.add(entity)
                    }
                }
            }
        }
    }
}
