package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.DemoPacketHandler
import ru.cherryngine.impl.demo.Player
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.StaticViewableProvider
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class ViewSystem(
    val demoPacketHandler: DemoPacketHandler,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val player = demoPacketHandler.players[playerComponent.uuid] ?: return
        val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf()
        val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf()

        world.family { all(ViewableComponent, ViewableProvidersEvent) }.forEach { viewableEntity ->
            val viewableComponent = viewableEntity[ViewableComponent]
            val viewableProvidersEvent = viewableEntity[ViewableProvidersEvent]

            if (playerComponent.viewContextIDs.any { it in viewableComponent.viewContextIDs }) {
                viewableProviders.addAll(viewableProvidersEvent.viewableProviders)
                staticViewableProviders.addAll(viewableProvidersEvent.staticViewableProviders)
            }
        }

        update(entity, player, viewableProviders, staticViewableProviders)
    }

    fun getStaticViewables(
        staticViewableProviders: Set<StaticViewableProvider>,
        chunkPos: ChunkPos,
    ): Set<StaticViewable> {
        return staticViewableProviders.flatMap { it.getStaticViewables(chunkPos) }.toSet()
    }

    fun getViewables(viewableProviders: Set<ViewableProvider>): Set<Viewable> {
        return viewableProviders.flatMap { it.viewables }.toSet()
    }

    fun update(
        entity: Entity,
        player: Player,
        viewableProviders: Set<ViewableProvider>,
        staticViewableProviders: Set<StaticViewableProvider>,
    ) {
        val connection = player.connection
        if (connection.state != ProtocolState.PLAY) return
        val distance = DEFAULT_RENDER_DISTANCE

        val clientChunkPos = entity.getOrNull(PositionComponent)
            ?.position
            ?.let { ChunkUtils.chunkPosFromVec3D(it) }
            ?: ChunkPos.ZERO

        val currentVisibleViewables = player.currentVisibleViewables
        val currentVisibleStaticViewables = player.currentVisibleStaticViewables

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