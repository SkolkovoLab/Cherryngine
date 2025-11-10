package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent

class WorldSystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithComponent(PlayerComponent::class).forEach { gameObject ->
            val playerComponent = gameObject[PlayerComponent::class]!!
            if (playerComponent.world != playerComponent.prevWorld) {
                println("changed world")
                if (playerComponent.prevWorld != null) {
                    playerComponent.staticViewableProviders -= playerComponent.prevWorld
                    playerComponent.viewableProviders -= playerComponent.prevWorld
                    println("removed prev world")
                }
                if (playerComponent.world != null) {
                    playerComponent.staticViewableProviders += playerComponent.world
                    playerComponent.viewableProviders += playerComponent.world
                    println("added new world")
                }
                gameObject[PlayerComponent::class] = playerComponent.copy(
                    prevWorld = playerComponent.world,
                )
            }
        }
    }
}