package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.instance.TickablePriority
import kotlin.time.Duration

@InstanceSingleton
@TickablePriority(stage = TickStage.GAME)
class EcsWorldTickable(private val world: EcsWorld) : Tickable {
    override fun tick(delta: Duration) = world.update(delta)
}
