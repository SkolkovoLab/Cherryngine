package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerGameComponent

class PacketLogGameSystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val gameObjects = gameScene.objectsWithComponent(PlayerGameComponent::class)
        gameObjects.forEach { gameObject ->
            val playerGameComponent = gameObject[PlayerGameComponent::class]!!
            println(playerGameComponent.packets)
        }
    }
}