package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.Viewable

data class CurrentVisibleComponent(
    val currentVisibleViewables: Set<Viewable>,
    val currentVisibleStaticViewables: Set<StaticViewable>,
) : GameComponent