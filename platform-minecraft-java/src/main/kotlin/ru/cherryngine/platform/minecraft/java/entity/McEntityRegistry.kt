package ru.cherryngine.platform.minecraft.java.entity

import ru.cherryngine.engine.core.instance.InstanceSingleton

@InstanceSingleton(platform = "minecraft")
class McEntityRegistry {
    private val entities = mutableSetOf<McEntity>()

    fun add(mcEntity: McEntity) { entities.add(mcEntity) }
    fun remove(mcEntity: McEntity) { entities.remove(mcEntity) }
    fun allEntities(): Collection<McEntity> = entities
}
