package ru.cherryngine.engine.mcprotocollib

import org.cloudburstmc.math.vector.Vector3i
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockChangeEntry
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundLevelChunkWithLightPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSectionBlocksUpdatePacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSetChunkCacheCenterPacket
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.ServerWorld
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.light.LightData
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.MutableLayerChangeTracker
import java.util.*
import kotlin.time.Duration

@InstanceSingleton(platform = "mcprotocollib", stage = TickStage.POST)
class McProtocolLibViewTickable(
    private val playerManager: PlayerManager,
    private val chunkPool: McProtocolLibChunkPool,
    private val entityRegistry: McProtocolLibEntityRegistry,
    private val serverWorld: ServerWorld,
) : Tickable {
    private val changeTracker: MutableLayerChangeTracker? = null

    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val mcplPlayer = player as? McProtocolLibPlayer ?: continue
            val playerContextIDs = mcplPlayer.viewContextIDs
            val layers = serverWorld.getLayersForContexts(playerContextIDs)
            val dimensionType = serverWorld.dimensionType

            val visibleEntities = entityRegistry.allEntities()
                .filter { entity ->
                    val ctx = entity.viewContextIDs
                    ctx.isEmpty() || ctx.any { it in playerContextIDs }
                }
                .toSet()

            update(mcplPlayer, visibleEntities, layers, dimensionType)
        }
    }

    private fun update(
        player: McProtocolLibPlayer,
        visibleEntities: Set<McProtocolLibEntity>,
        layers: List<LayerEntry>,
        dimensionType: DimensionType?,
    ) {
        val distance = DEFAULT_RENDER_DISTANCE
        val clientChunkPos = ChunkUtils.chunkPosFromVec3D(player.clientPosition)

        if (player.sentChunkCacheCenter != clientChunkPos) {
            player.sentChunkCacheCenter = clientChunkPos
            player.session.send(ClientboundSetChunkCacheCenterPacket(clientChunkPos.x, clientChunkPos.z))
        }

        val currentVisible = player.currentVisibleEntities
        val chunks = ChunkUtils.getChunksInRange(clientChunkPos, distance).toSet()

        // Hide entities no longer visible
        currentVisible.removeIf { entity ->
            val shouldHide = entity !in visibleEntities ||
                entity.chunkPos !in chunks ||
                !entity.viewerPredicate(player)
            if (shouldHide) entity.hide(player)
            shouldHide
        }

        // Send chunks
        if (layers.isNotEmpty() && dimensionType != null) {
            val classification = LayerClassification.classify(layers)
            val playerSentChunks = player.sentChunks

            val currentKey = classification.immutableKey
            if (player.sentChunksBase != currentKey) {
                player.sentChunksBase = currentKey
                playerSentChunks.clear()
            }

            playerSentChunks -= player.chunksToRefresh

            val dirtyChunks = changeTracker?.getDirty() ?: emptyMap()
            val mutableLayerIds = classification.mutableLayers.map { it.layer.id }.toSet()
            val dirtyForPlayer = dirtyChunks.filterKeys { it in mutableLayerIds }.values.flatten().toSet()
            val alreadySentDirty = dirtyForPlayer.intersect(playerSentChunks).intersect(chunks)

            playerSentChunks.retainAll(chunks)

            val chunksToSend = chunks - playerSentChunks
            for (chunkPos in chunksToSend) {
                val baseChunkData = chunkPool.get(
                    classification.immutableKey, chunkPos, dimensionType, classification.immutableLayers
                )
                val lightData = chunkPool.getLightData(classification.immutableLayers, chunkPos) ?: LightData.EMPTY
                sendChunk(player, chunkPos, baseChunkData, lightData)
                sendMutableOverlay(player, classification, dimensionType, chunkPos, baseChunkData)
                playerSentChunks.add(chunkPos)
            }

            for (chunkPos in alreadySentDirty) {
                val baseChunkData = chunkPool.get(
                    classification.immutableKey, chunkPos, dimensionType, classification.immutableLayers
                )
                sendMutableOverlay(player, classification, dimensionType, chunkPos, baseChunkData)
            }
        }

        // Show new entities
        visibleEntities.forEach { entity ->
            val shouldShow = entity !in currentVisible &&
                entity.chunkPos in chunks &&
                entity.viewerPredicate(player)
            if (shouldShow) {
                entity.show(player)
                currentVisible.add(entity)
            }
        }

        player.chunksToRefresh.clear()
    }

    private fun sendChunk(
        player: McProtocolLibPlayer,
        chunkPos: ChunkPos,
        chunkData: ChunkData,
        lightData: LightData?,
    ) {
        val chunkBytes = McProtocolLibChunkSerializer.serializeSections(chunkData.sections)
        val heightmaps = McProtocolLibChunkSerializer.convertHeightmaps(chunkData.heightmaps)
        val emptyLight = LightUpdateData(BitSet(), BitSet(), BitSet(), BitSet(), emptyList(), emptyList())

        player.session.send(
            ClientboundLevelChunkWithLightPacket(
                chunkPos.x, chunkPos.z,
                chunkBytes,
                heightmaps,
                emptyArray(),
                emptyLight
            )
        )
    }

    private fun sendMutableOverlay(
        player: McProtocolLibPlayer,
        classification: LayerClassification,
        dimensionType: DimensionType,
        chunkPos: ChunkPos,
        baseChunkData: ChunkData,
    ) {
        if (classification.mutableLayers.isEmpty()) return
        val overlay = MutableOverlay.computeOverlay(classification, dimensionType, chunkPos, baseChunkData)
        for ((sectionPos, changes) in overlay) {
            sendSectionBlocksUpdate(player, sectionPos, changes)
        }
    }

    private fun sendSectionBlocksUpdate(
        player: McProtocolLibPlayer,
        sectionPos: SectionPos,
        changes: List<Long>,
    ) {
        if (changes.isEmpty()) return
        val entries = changes.map { encoded ->
            val blockStateId = (encoded shr 12).toInt()
            val localX = ((encoded shr 8) and 0xF).toInt()
            val localZ = ((encoded shr 4) and 0xF).toInt()
            val localY = (encoded and 0xF).toInt()
            val globalX = sectionPos.x * 16 + localX
            val globalY = sectionPos.y * 16 + localY
            val globalZ = sectionPos.z * 16 + localZ
            BlockChangeEntry(Vector3i.from(globalX, globalY, globalZ), blockStateId)
        }.toTypedArray()

        player.session.send(
            ClientboundSectionBlocksUpdatePacket(sectionPos.x, sectionPos.y, sectionPos.z, *entries)
        )
    }
}
