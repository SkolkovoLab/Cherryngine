package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.core.Tickable
import kotlin.time.Duration

class EcsWorldTickable(private val world: EcsWorld) : Tickable {
    override fun tick(delta: Duration) = world.update(delta)
}
