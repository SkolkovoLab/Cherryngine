package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent

data class WorldComponent(
    val worldName: String,
) : GameComponent