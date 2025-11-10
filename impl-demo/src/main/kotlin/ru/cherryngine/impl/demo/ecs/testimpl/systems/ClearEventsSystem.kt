package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.components.EventsComponent

class ClearEventsSystem : IteratingSystem(
    family { all(EventsComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val eventsComponent = entity[EventsComponent]
        eventsComponent.events.clear()
    }
}