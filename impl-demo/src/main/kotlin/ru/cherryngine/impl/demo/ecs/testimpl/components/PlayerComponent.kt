package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.lib.minecraft.server.Connection

data class PlayerComponent(
    val connection: Connection,
    val viewContextID: String,
) : GameComponent