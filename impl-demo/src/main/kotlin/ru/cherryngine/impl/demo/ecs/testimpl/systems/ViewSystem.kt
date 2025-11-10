package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameObject
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.CurrentVisibleComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class ViewSystem(
    val gameScene: GameScene,
) : GameSystem {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val gameObjects = gameScene.objectsWithComponent(PlayerComponent::class)
        gameObjects.forEach { gameObject ->
            val playerComponent = gameObject[PlayerComponent::class]!!
            update(gameObject, playerComponent)
        }
    }

    fun getStaticViewables(player: PlayerComponent, chunkPos: ChunkPos): Set<StaticViewable> {
        return player.staticViewableProviders.flatMap { it.getStaticViewables(chunkPos) }.toSet()
    }

    fun getViewables(player: PlayerComponent): Set<Viewable> {
        return player.viewableProviders.flatMap { it.viewables }.toSet()
    }

    fun update(gameObject: GameObject, playerComponent: PlayerComponent) {
        val connection = playerComponent.connection
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
        val viewables: Set<Viewable> = getViewables(playerComponent)

        currentVisibleStatic.removeIf { staticViewable ->
            val staticViewables = getStaticViewables(playerComponent, staticViewable.chunkPos)
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
            val staticViewables = getStaticViewables(playerComponent, chunk)
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