package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent

class PacketLogSystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val gameObjects = gameScene.objectsWithComponent(PlayerComponent::class)
        gameObjects.forEach { gameObject ->
            val playerComponent = gameObject[PlayerComponent::class]!!
            println(playerComponent.packets)
        }
    }
}