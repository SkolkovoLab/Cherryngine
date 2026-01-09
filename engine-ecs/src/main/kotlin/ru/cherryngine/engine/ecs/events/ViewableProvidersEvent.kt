package ru.cherryngine.engine.ecs.events

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.core.view.StaticViewableProvider
import ru.cherryngine.engine.core.view.ViewableProvider
import ru.cherryngine.lib.world.World

class ViewableProvidersEvent(
    val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf(),
    val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf(),
    val worlds: MutableSet<World> = mutableSetOf(),
) : EcsEvent<ViewableProvidersEvent> {
    override fun type() = ViewableProvidersEvent

    companion object : ComponentType<ViewableProvidersEvent>()
}