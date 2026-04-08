package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.engine.core.view.StaticViewableProvider
import ru.cherryngine.engine.core.view.Viewable
import ru.cherryngine.engine.core.view.ViewableProvider
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent
import ru.cherryngine.engine.ecs.components.PositionComponent
import ru.cherryngine.engine.ecs.components.ViewableComponent
import ru.cherryngine.engine.ecs.events.ViewableProvidersEvent
import ru.cherryngine.lib.minecraft.network.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.light.LightData
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.LayeredWorld

class ViewSystem(
    val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    override fun onTickEntity(entity: EcsEntity) {
        val playerComponent = entity[PlayerComponent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) ?: return
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
        player: Player,
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
            val world = LayeredWorld(dimensionType!!, layers)
            chunks.forEach { chunkPos ->
                val chunkData = world.getChunkData(chunkPos)
                val lightData = world.getLightData(chunkPos) ?: LightData.EMPTY
                player.connection.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, chunkData, lightData))
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
}
