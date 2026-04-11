package ru.cherryngine.engine.minecraft.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.minecraft.ChunkPool
import ru.cherryngine.engine.minecraft.MinecraftWorldServiceHandler
import ru.cherryngine.engine.minecraft.entity.McEntityRegistry
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.engine.minecraft.view.BlocksViewable
import ru.cherryngine.engine.minecraft.view.Viewable
import ru.cherryngine.engine.minecraft.world.LayerClassification
import ru.cherryngine.engine.minecraft.world.MutableOverlay
import ru.cherryngine.lib.minecraft.network.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundSectionBlocksUpdatePacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.light.LightData
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.MutableLayerChangeTracker

class MinecraftViewSystem(
    val playerManager: PlayerManager,
    val chunkPool: ChunkPool,
    val worldServiceHandler: MinecraftWorldServiceHandler,
    val mcEntityRegistry: McEntityRegistry,
    val changeTracker: MutableLayerChangeTracker? = null,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    override fun onTickEntity(entity: EcsEntity) {
        val playerComponent = entity[PlayerComponent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) as? MinecraftPlayer ?: return

        val layers = worldServiceHandler.getLayersForPlayer(playerComponent.uuid)
        val dimensionType = worldServiceHandler.dimensionType
        val playerContextIDs = playerComponent.viewContextIDs
        val viewables: Set<Viewable> = mcEntityRegistry.allEntities()
            .filter { entity ->
                val entityContexts = entity.viewContextIDs
                entityContexts.isEmpty() || entityContexts.any { it in playerContextIDs }
            }
            .toSet()

        update(entity, player, viewables, layers, dimensionType)
    }

    fun update(
        entity: EcsEntity,
        player: MinecraftPlayer,
        viewables: Set<Viewable>,
        layers: List<LayerEntry>,
        dimensionType: DimensionType?,
    ) {
        val connection = player.connection
        if (connection.state != ProtocolState.PLAY) return
        val distance = DEFAULT_RENDER_DISTANCE

        val clientChunkPos = entity.getOrNull(PositionComponent)
            ?.position
            ?.let { ChunkUtils.chunkPosFromVec3D(it) }
            ?: ChunkPos.ZERO

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
                val lightData = chunkPool.getLightData(classification.immutableLayers, chunkPos) ?: LightData.EMPTY
                player.connection.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, baseChunkData, lightData))

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
            player.connection.sendPacket(ClientboundSectionBlocksUpdatePacket(sectionPos, blockChanges))
        }
    }
}
