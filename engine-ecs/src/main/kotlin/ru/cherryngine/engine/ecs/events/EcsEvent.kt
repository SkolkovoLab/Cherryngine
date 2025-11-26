package ru.cherryngine.engine.ecs.events

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ru.cherryngine.engine.ecs.EcsComponent
import ru.cherryngine.engine.ecs.systems.ClearEventsSystem

interface EcsEvent<T : EcsEvent<T>> : EcsComponent<T> {
    override fun World.onAdd(entity: Entity) {
        val eventsSystem = system<ClearEventsSystem>()
        eventsSystem.putEvent(entity, this@EcsEvent)
    }
}