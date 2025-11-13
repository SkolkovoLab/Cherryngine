package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.DemoWorlds
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent

class WorldSystem(
    val demoWorlds: DemoWorlds,
) : IteratingSystem(
    family { all(WorldComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val worldComponent = entity[WorldComponent]
        val worldName = worldComponent.worldName
        val world = demoWorlds.worlds[worldName] ?: return
        entity.configure {
            val event = it.getOrAdd(ViewableProvidersEvent, ::ViewableProvidersEvent)
            event.staticViewableProviders += world
        }
    }
}