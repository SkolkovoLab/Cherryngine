package ru.cherryngine.platform.minecraft.java.entity

import ru.cherryngine.engine.core.instance.InstanceSingleton

@InstanceSingleton(platform = "minecraft")
class McEntityRegistry {
    private val entities = mutableSetOf<McEntity>()

    fun add(mcEntity: McEntity) { entities.add(mcEntity) }

    /**
     * Удаляет entity из реестра. Перед удалением скрывает её у всех текущих
     * viewers (шлёт DestroyEntitiesPacket). Это инвариант: если entity больше
     * нет в реестре — её ни у одного клиента не должно остаться. MinecraftViewTickable
     * итерирует ТОЛЬКО реестр и не имеет шанса скрыть entity, которой там уже нет.
     */
    fun remove(mcEntity: McEntity) {
        mcEntity.viewers.toList().forEach { mcEntity.hide(it) }
        entities.remove(mcEntity)
    }

    fun allEntities(): Collection<McEntity> = entities
}
