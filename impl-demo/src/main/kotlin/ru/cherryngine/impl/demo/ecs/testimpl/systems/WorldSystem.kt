package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.world.TestWorldShit

class WorldSystem(
    val gameScene: GameScene,
    val testWorldShit: TestWorldShit,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithComponent(WorldComponent::class).forEach { (gameObject, worldComponent) ->
            val worldName = worldComponent.worldName
            val world = testWorldShit.worlds[worldName] ?: return@forEach
            gameObject.setEvent(ViewableProvidersEvent::class, ViewableProvidersEvent(setOf(), setOf(world)))
        }
    }
}