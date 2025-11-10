package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.impl.demo.entity.McEntity

data class EntityComponent(
    val entity: McEntity,
) : GameComponent