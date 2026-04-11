package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.minecraft.view.BlocksViewable
import ru.cherryngine.engine.minecraft.view.StaticViewableProvider
import ru.cherryngine.engine.minecraft.view.Viewable
import ru.cherryngine.engine.minecraft.view.ViewableProvider
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.ecs.components.ViewableComponent
import ru.cherryngine.engine.ecs.events.ViewableProvidersEvent
import ru.cherryngine.lib.minecraft.network.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.light.LightData
import ru.cherryngine.engine.minecraft.ChunkPool
import ru.cherryngine.engine.minecraft.world.LayerClassification
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.MutableLayerChangeTracker
import ru.cherryngine.engine.minecraft.world.MutableOverlay
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundSectionBlocksUpdatePacket

class ViewSystem(
    val playerManager: PlayerManager,
    val chunkPool: ChunkPool,
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

        val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf()
        val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf()
        val layers: MutableList<LayerEntry> = mutableListOf()
        var dimensionType: DimensionType? = null

        playerComponent.viewContextIDs.forEach { viewContextID ->
            world.family { all(ViewableComponent, ViewableProvidersEvent) }.forEach { viewableEntity ->
                val viewableComponent = viewableEntity[ViewableComponent]
                val viewableProvidersEvent = viewableEntity[ViewableProvidersEvent]

                if (viewContextID in viewableComponent.viewContextIDs) {
                    viewableProviders.addAll(viewableProvidersEvent.viewableProviders)
                    staticViewableProviders.addAll(viewableProvidersEvent.staticViewableProviders)
                    layers.addAll(viewableProvidersEvent.layers)
                    if (dimensionType == null) dimensionType = viewableProvidersEvent.dimensionType
                }
            }
        }

        update(entity, player, viewableProviders, staticViewableProviders, layers, dimensionType)
    }

    fun getStaticViewables(
        staticViewableProviders: Set<StaticViewableProvider>,
        chunkPos: ChunkPos,
    ): Set<BlocksViewable> {
        return staticViewableProviders.flatMap { it.getStaticViewables(chunkPos) }.toSet()
    }

    fun getViewables(viewableProviders: Set<ViewableProvider>): Set<Viewable> {
        return viewableProviders.flatMap { it.viewables }.toSet()
    }

    fun update(
        entity: EcsEntity,
        player: MinecraftPlayer,
        viewableProviders: Set<ViewableProvider>,
        staticViewableProviders: Set<StaticViewableProvider>,
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
        val viewables: Set<Viewable> = getViewables(viewableProviders)

        currentVisibleStaticViewables.removeIf { staticViewable ->
            val staticViewables = getStaticViewables(staticViewableProviders, staticViewable.chunkPos)
            val shouldHide = staticViewable !in staticViewables ||
                    staticViewable.chunkPos !in chunks ||
                    !staticViewable.viewerPredicate(player)
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

            // Если изменился набор immutable слоёв — клиент видит устаревший base, шлём всё заново
            val currentKey = classification.immutableKey
            if (player.sentChunksBase != currentKey) {
                player.sentChunksBase = currentKey
                playerSentChunks.clear()
            }

            // Добавляем чанки из chunksToRefresh в очередь на переотправку
            playerSentChunks -= player.chunksToRefresh

            // Dirty чанки из mutable слоёв — тоже нужно переотправить overlay
            val dirtyChunks = changeTracker?.getDirty() ?: emptyMap()
            val mutableLayerIds = classification.mutableLayers.map { it.layer.id }.toSet()
            val dirtyForPlayer = dirtyChunks.filterKeys { it in mutableLayerIds }.values.flatten().toSet()
            val alreadySentDirty = dirtyForPlayer.intersect(playerSentChunks).intersect(chunks)

            // Убираем чанки вышедшие из радиуса видимости
            playerSentChunks.retainAll(chunks)

            // Отправка новых чанков: immutable base из пула + mutable overlay
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

            // Обновление dirty чанков из mutable слоёв (уже отправленные ранее)
            for (chunkPos in alreadySentDirty) {
                val baseChunkData = chunkPool.get(
                    classification.immutableKey, chunkPos, dimensionType, classification.immutableLayers
                )
                sendMutableOverlay(player, classification, dimensionType, chunkPos, baseChunkData)
            }
        }

        chunks.forEach { chunkPos ->
            val staticViewables = getStaticViewables(staticViewableProviders, chunkPos)
            staticViewables.forEach { staticViewable ->
                val shouldShow =
                    (staticViewable !in currentVisibleStaticViewables || chunkPos in player.chunksToRefresh) &&
                            staticViewable.viewerPredicate(player)
                if (shouldShow) {
                    staticViewable.show(player)
                    currentVisibleStaticViewables.add(staticViewable)
                }
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
