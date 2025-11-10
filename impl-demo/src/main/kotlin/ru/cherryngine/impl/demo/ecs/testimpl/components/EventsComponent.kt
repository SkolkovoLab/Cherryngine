package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.impl.demo.ecs.GameEvent
import kotlin.reflect.KClass

data class EventsComponent(
    val events: MutableMap<KClass<out GameEvent>, GameEvent> = mutableMapOf(),
) : Component<EventsComponent> {
    override fun type() = EventsComponent

    companion object : ComponentType<EventsComponent>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T : GameEvent> get(type: KClass<T>): T? {
        return events[type] as T?
    }

    operator fun <T : GameEvent> set(type: KClass<T>, event: T) {
        events[type] = event
    }
}