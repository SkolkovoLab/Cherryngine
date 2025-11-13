package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.events.Events

class ClearEventsSystem : IteratingSystem(
    family { any(*Events.types.toTypedArray()) }
) {
    override fun onTickEntity(entity: Entity) {
        entity.configure {
            Events.types.forEach { type ->
                @Suppress("UNCHECKED_CAST")
                it -= type as ComponentType<Component<Any>>
            }
        }
    }
}