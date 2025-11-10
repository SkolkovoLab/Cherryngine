package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameObject
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.CurrentVisibleComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.StaticViewableProvider
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.protocol.packets.ProtocolState
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class ViewSystem(
    val gameScene: GameScene,
) : GameSystem {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithComponent(PlayerComponent::class).forEach { playerGameObject ->
            val playerComponent = playerGameObject[PlayerComponent::class]!!

            val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf()
            val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf()

            gameScene.objectsWithComponent(ViewableComponent::class).forEach { viewableGameObject ->
                val viewableComponent = viewableGameObject[ViewableComponent::class]!!
                if (playerComponent.viewContextID != viewableComponent.viewContextID) return@forEach
                viewableProviders.addAll(viewableComponent.viewableProviders)
                staticViewableProviders.addAll(viewableComponent.staticViewableProviders)
            }

            update(playerGameObject, playerComponent.connection, viewableProviders, staticViewableProviders)
        }
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
        gameObject: GameObject,
        connection: Connection,
        viewableProviders: Set<ViewableProvider>,
        staticViewableProviders: Set<StaticViewableProvider>,
    ) {
        if (connection.state != ProtocolState.PLAY) return
        val distance = DEFAULT_RENDER_DISTANCE

        val clientChunkPos = gameObject[ClientPositionComponent::class]
            ?.clientPosition
            ?.let { ChunkUtils.chunkPosFromVec3D(it) }
            ?: ChunkPos.ZERO

        val currentVisibleComponent = gameObject[CurrentVisibleComponent::class]
        val currentVisible = currentVisibleComponent?.currentVisibleViewables?.toMutableSet() ?: mutableSetOf()
        val currentVisibleStatic =
            currentVisibleComponent?.currentVisibleStaticViewables?.toMutableSet() ?: mutableSetOf()

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

        gameObject[CurrentVisibleComponent::class] = CurrentVisibleComponent(currentVisible, currentVisibleStatic)
    }
}