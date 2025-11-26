package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.IntervalSystem
import ru.cherryngine.engine.ecs.EcsComponent
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.events.EcsEvent

class ClearEventsSystem : IntervalSystem() {
    private val toRemove = hashMapOf<EcsEntity, MutableSet<ComponentType<*>>>()
    override fun onTick() {
        toRemove.forEach { (entity, types) ->
            entity.configure {
                types.forEach { type ->
                    @Suppress("UNCHECKED_CAST")
                    type as ComponentType<EcsComponent<Any>>
                    it.minusAssign<EcsComponent<Any>>(type)
                }
            }
        }
        toRemove.clear()
    }

    internal fun putEvent(entity: EcsEntity, event: EcsEvent<*>) {
        val types = toRemove.computeIfAbsent(entity) { hashSetOf() }
        types += event.type()
    }
}