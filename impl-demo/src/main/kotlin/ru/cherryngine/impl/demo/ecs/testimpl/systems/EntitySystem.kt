package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.EntityComponent

class EntitySystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val gameObjects = gameScene.objectsWithComponent(EntityComponent::class)
        gameObjects.forEach { gameObject ->
            val entityComponent = gameObject[EntityComponent::class]!!
            val posComponent = gameObject[ClientPositionComponent::class] ?: return@forEach
            entityComponent.entity.teleport(posComponent.clientPosition, posComponent.clientYawPitch)
        }
    }
}