package ru.cherryngine.engine.ecs

import io.micronaut.context.annotation.Factory
import ru.cherryngine.engine.core.instance.Instance
import ru.cherryngine.engine.core.instance.InstanceSingleton

@Factory
class EcsWorldBeanFactory {

    @InstanceSingleton
    fun ecsWorld(instance: Instance): EcsWorld = instance.get()
}
