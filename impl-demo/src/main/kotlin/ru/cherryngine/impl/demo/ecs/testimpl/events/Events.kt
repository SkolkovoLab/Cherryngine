package ru.cherryngine.impl.demo.ecs.testimpl.events

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

object Events {
    val types: Set<ComponentType<out Component<*>>> = setOf(
        PacketsEvent,
        ViewableProvidersEvent
    )
}