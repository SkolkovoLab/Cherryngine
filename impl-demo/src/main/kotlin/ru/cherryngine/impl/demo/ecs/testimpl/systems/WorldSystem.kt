package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.eventsComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.world.TestWorldShit

class WorldSystem(
    val testWorldShit: TestWorldShit,
) : IteratingSystem(
    family { all(WorldComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val worldComponent = entity[WorldComponent]
        val worldName = worldComponent.worldName
        val world = testWorldShit.worlds[worldName] ?: return
        entity.eventsComponent()[ViewableProvidersEvent::class] = ViewableProvidersEvent(setOf(), setOf(world))
    }
}