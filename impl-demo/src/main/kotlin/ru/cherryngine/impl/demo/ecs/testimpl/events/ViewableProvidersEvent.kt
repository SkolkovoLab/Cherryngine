package ru.cherryngine.impl.demo.ecs.testimpl.events

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.core.view.StaticViewableProvider
import ru.cherryngine.engine.core.view.ViewableProvider

class ViewableProvidersEvent(
    val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf(),
    val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf(),
) : Component<ViewableProvidersEvent> {
    override fun type() = ViewableProvidersEvent

    companion object : ComponentType<ViewableProvidersEvent>()
}