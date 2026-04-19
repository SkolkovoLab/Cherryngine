package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import kotlin.time.Duration

@InstanceSingleton(stage = TickStage.GAME)
class EcsWorldTickable(private val world: EcsWorld) : Tickable {
    override fun tick(delta: Duration) = world.update(delta)
}
