package ru.cherryngine.platform.minecraft.java

import io.micronaut.context.annotation.Factory
import ru.cherryngine.engine.core.instance.Instance
import ru.cherryngine.engine.core.instance.InstanceSingleton

/**
 * Per-instance factory для платформенно-специфичных бинов engine-minecraft,
 * которые живут в cache через `Instance.register(...)`, но должны резолвиться
 * и через Micronaut DI (например, как constructor-параметр `@InstanceSingleton`).
 */
@Factory
class MinecraftInstanceBeansFactory {

    @InstanceSingleton
    fun minecraftServerWorld(instance: Instance): MinecraftServerWorld = instance.get()
}
