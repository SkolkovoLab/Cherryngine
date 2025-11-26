package ru.cherryngine.engine.ecs.events

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.core.view.StaticViewableProvider
import ru.cherryngine.engine.core.view.ViewableProvider

class ViewableProvidersEvent(
    val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf(),
    val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf(),
) : EcsEvent<ViewableProvidersEvent> {
    override fun type() = ViewableProvidersEvent

    companion object : ComponentType<ViewableProvidersEvent>()
}