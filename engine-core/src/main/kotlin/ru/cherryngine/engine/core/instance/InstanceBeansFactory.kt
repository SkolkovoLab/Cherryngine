package ru.cherryngine.engine.core.instance

import io.micronaut.context.annotation.Factory
import ru.cherryngine.engine.core.world.ServerWorld

@Factory
class InstanceBeansFactory {

    @InstanceSingleton
    fun instance(): Instance = InstanceSingletonScope.currentInstance()
        ?: error("Instance requested outside of an active Instance scope")

    @InstanceSingleton
    fun serverWorld(instance: Instance): ServerWorld = instance.get()
}
