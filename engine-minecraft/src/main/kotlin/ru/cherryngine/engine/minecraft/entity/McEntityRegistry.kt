package ru.cherryngine.engine.minecraft.entity

import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class McEntityRegistry {
    private val entities = HashMap<UUID, McEntity>()

    fun getOrCreate(mcEntityId: UUID, factory: () -> McEntity): McEntity {
        return entities.getOrPut(mcEntityId, factory)
    }

    fun get(mcEntityId: UUID): McEntity? = entities[mcEntityId]

    fun remove(mcEntityId: UUID) {
        entities.remove(mcEntityId)
    }

    fun allEntities(): Collection<McEntity> = entities.values
}
