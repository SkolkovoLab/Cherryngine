package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.impl.demo.view.StaticViewableProvider
import ru.cherryngine.impl.demo.view.ViewableProvider

data class ViewableComponent(
    val viewContextID: String,
    val viewableProviders: Set<ViewableProvider>,
    val staticViewableProviders: Set<StaticViewableProvider>,
) : GameComponent