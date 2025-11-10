package ru.cherryngine.impl.demo.ecs.testimpl.events

import ru.cherryngine.impl.demo.ecs.GameEvent
import ru.cherryngine.impl.demo.view.StaticViewableProvider
import ru.cherryngine.impl.demo.view.ViewableProvider

data class ViewableProvidersEvent(
    val viewableProviders: Set<ViewableProvider>,
    val staticViewableProviders: Set<StaticViewableProvider>,
) : GameEvent