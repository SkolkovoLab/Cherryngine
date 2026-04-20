package ru.cherryngine.engine.minecraft

import net.minestom.server.network.ConnectionState
import net.minestom.server.network.packet.server.play.ChunkDataPacket
import net.minestom.server.network.packet.server.play.MultiBlockChangePacket
import net.minestom.server.network.packet.server.play.UpdateViewPositionPacket
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.registry.Registries
import net.minestom.server.world.DimensionType
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.minecraft.entity.McEntityRegistry
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.engine.minecraft.view.Viewable
import ru.cherryngine.engine.minecraft.world.LayerClassification
import ru.cherryngine.engine.minecraft.world.MutableOverlay
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.ChunkPos
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.MutableLayerChangeTracker
import java.util.BitSet
import kotlin.time.Duration

@InstanceSingleton(platform = "minecraft", stage = TickStage.POST)
class MinecraftViewTickable(
    private val playerManager: PlayerManager,
    private val chunkPool: ChunkPool,
    private val mcEntityRegistry: McEntityRegistry,
    private val serverWorld: MinecraftServerWorld,
    private val registries: Registries,
) : Tickable {
    private val changeTracker: MutableLayerChangeTracker? = null
    private val biomeCount: Int = registries.biome().size()

    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2

        private val EMPTY_LIGHT_DATA = LightData(
            BitSet(), BitSet(), BitSet(), BitSet(),
            emptyList(), emptyList()
        )
    }

    override fun tick(delta: Duration) {
        for (player in playerManager.onlinePlayers()) {
            val mcPlayer = player as? MinecraftPlayer ?: continue
            val playerContextIDs = mcPlayer.viewContextIDs
            val layers = serverWorld.getLayersForContexts(playerContextIDs)
            val dimensionType = serverWorld.dimensionType
            val viewables = mcEntityRegistry.allEntities()
                .filter { entity ->
                    val ctx = entity.viewContextIDs
                    ctx.isEmpty() || ctx.any { it in playerContextIDs }
                }
                .toSet()

            update(mcPlayer, viewables, layers, dimensionType)
        }
    }

    private fun update(
        player: MinecraftPlayer,
        viewables: Set<Viewable>,
        layers: List<LayerEntry>,
        dimensionType: DimensionType?,
    ) {
        val connection = player.connection
        if (connection.state != ConnectionState.PLAY) return
        val distance = DEFAULT_RENDER_DISTANCE

        val clientChunkPos = ChunkUtils.chunkPosFromVec3D(player.clientPosition)

        if (player.sentChunkCacheCenter != clientChunkPos) {
            player.sentChunkCacheCenter = clientChunkPos
            connection.sendPacket(UpdateViewPositionPacket(clientChunkPos.x, clientChunkPos.z))
        }

        val currentVisibleViewables = player.currentVisibleViewables
        val currentVisibleStaticViewables = player.currentVisibleBlocksViewables

        val chunks = ChunkUtils.getChunksInRange(clientChunkPos, distance).toSet()

        currentVisibleStaticViewables.removeIf { staticViewable ->
            val shouldHide = staticViewable.chunkPos !in chunks || !staticViewable.viewerPredicate(player)
            if (shouldHide) staticViewable.hide(player)
            shouldHide
        }

        currentVisibleViewables.removeIf { viewable ->
            val shouldHide =
                viewable !in viewables || viewable.chunkPos !in chunks || !viewable.viewerPredicate(player)
            if (shouldHide) viewable.hide(player)
            shouldHide
        }

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
                val lightData = chunkPool.getLightData(classification.immutableLayers, chunkPos) ?: EMPTY_LIGHT_DATA
                player.connection.sendPacket(
                    ChunkDataPacket(
                        chunkPos.x,
                        chunkPos.z,
                        baseChunkData.toMinestomChunkData(biomeCount),
                        lightData,
                    )
                )

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

        viewables.forEach { viewable ->
            val shouldShow = viewable !in currentVisibleViewables &&
                    viewable.chunkPos in chunks &&
                    viewable.viewerPredicate(player)
            if (shouldShow) {
                viewable.show(player)
                currentVisibleViewables.add(viewable)
            }
        }

        player.chunksToRefresh.clear()
    }

    private fun sendMutableOverlay(
        player: MinecraftPlayer,
        classification: LayerClassification,
        dimensionType: DimensionType,
        chunkPos: ChunkPos,
        baseChunkData: ChunkData,
    ) {
        if (classification.mutableLayers.isEmpty()) return
        val overlay = MutableOverlay.computeOverlay(classification, dimensionType, chunkPos, baseChunkData)
        for ((sectionPos, blockChanges) in overlay) {
            player.connection.sendPacket(
                MultiBlockChangePacket(sectionPos.x, sectionPos.y, sectionPos.z, blockChanges.toLongArray())
            )
        }
    }
}
