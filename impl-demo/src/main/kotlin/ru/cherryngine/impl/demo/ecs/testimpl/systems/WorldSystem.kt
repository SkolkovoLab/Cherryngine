package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerGameComponent

class WorldSystem(
    val gameScene: GameScene,
) : GameSystem{
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithComponent(PlayerGameComponent::class).forEach { gameObject ->
            val playerGameComponent = gameObject[PlayerGameComponent::class]!!
            val viewSystem = playerGameComponent.viewSystem
            if (playerGameComponent.world != playerGameComponent.prevWorld) {
                println("changed world")
                if (playerGameComponent.prevWorld != null) {
                    viewSystem.staticViewableProviders -= playerGameComponent.prevWorld
                    viewSystem.viewableProviders -= playerGameComponent.prevWorld
                    println("removed prev world")
                }
                if (playerGameComponent.world != null) {
                    viewSystem.staticViewableProviders += playerGameComponent.world
                    viewSystem.viewableProviders += playerGameComponent.world
                    println("added new world")
                }
                gameObject[PlayerGameComponent::class] = playerGameComponent.copy(
                    prevWorld = playerGameComponent.world,
                )
            }
            viewSystem.update()
        }
    }
}