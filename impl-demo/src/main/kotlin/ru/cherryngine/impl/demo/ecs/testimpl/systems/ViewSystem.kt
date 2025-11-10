package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.EventsComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.StaticViewableProvider
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class ViewSystem : IteratingSystem(
    family { all(PlayerComponent) }
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    // TODO сделать чтоб не утекало
    val currentVisibleViewablesMap = hashMapOf<Connection, MutableSet<Viewable>>()
    val currentVisibleStaticViewablesMap = hashMapOf<Connection, MutableSet<StaticViewable>>()

    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf()
        val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf()

        world.family { all(ViewableComponent, EventsComponent) }.forEach { viewableEntity ->
            val viewableComponent = viewableEntity[ViewableComponent]
            val eventsComponent = viewableEntity[EventsComponent]
            val viewableProvidersEvent = eventsComponent[ViewableProvidersEvent::class] ?: return@forEach

            if (playerComponent.viewContextID != viewableComponent.viewContextID) return@forEach
            viewableProviders.addAll(viewableProvidersEvent.viewableProviders)
            staticViewableProviders.addAll(viewableProvidersEvent.staticViewableProviders)
        }

        update(entity, playerComponent.connection, viewableProviders, staticViewableProviders)
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
        connection: Connection,
        viewableProviders: Set<ViewableProvider>,
        staticViewableProviders: Set<StaticViewableProvider>,
    ) {
        if (connection.state != ProtocolState.PLAY) return
        val distance = DEFAULT_RENDER_DISTANCE

        val clientChunkPos = entity.getOrNull(ClientPositionComponent)
            ?.clientPosition
            ?.let { ChunkUtils.chunkPosFromVec3D(it) }
            ?: ChunkPos.ZERO

        val currentVisible = currentVisibleViewablesMap.computeIfAbsent(connection) { mutableSetOf() }
        val currentVisibleStatic = currentVisibleStaticViewablesMap.computeIfAbsent(connection) { mutableSetOf() }

        val chunks = ChunkUtils.getChunksInRange(clientChunkPos, distance).toSet()
        val viewables: Set<Viewable> = getViewables(viewableProviders)

        currentVisibleStatic.removeIf { staticViewable ->
            val staticViewables = getStaticViewables(staticViewableProviders, staticViewable.chunkPos)
            val shouldHide =
                staticViewable !in staticViewables || staticViewable.chunkPos !in chunks || !staticViewable.viewerPredicate(
                    connection
                )
            if (shouldHide) staticViewable.hide(connection)
            shouldHide
        }

        currentVisible.removeIf { viewable ->
            val shouldHide =
                viewable !in viewables || viewable.chunkPos !in chunks || !viewable.viewerPredicate(connection)
            if (shouldHide) viewable.hide(connection)
            shouldHide
        }

        chunks.forEach { chunk ->
            val staticViewables = getStaticViewables(staticViewableProviders, chunk)
            staticViewables.forEach { staticViewable ->
                val shouldShow = staticViewable !in currentVisibleStatic && staticViewable.viewerPredicate(connection)
                if (shouldShow) {
                    staticViewable.show(connection)
                    currentVisibleStatic.add(staticViewable)
                }
            }
        }

        viewables.forEach { viewable ->
            val shouldShow =
                viewable !in currentVisible && viewable.chunkPos in chunks && viewable.viewerPredicate(connection)
            if (shouldShow) {
                viewable.show(connection)
                currentVisible.add(viewable)
            }
        }
    }
}