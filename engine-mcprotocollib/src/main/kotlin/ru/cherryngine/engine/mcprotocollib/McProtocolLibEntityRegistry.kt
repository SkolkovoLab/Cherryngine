package ru.cherryngine.engine.mcprotocollib

import java.util.UUID

class McProtocolLibEntityRegistry {
    private val entities = HashMap<UUID, McProtocolLibEntity>()

    fun getOrCreate(id: UUID, factory: () -> McProtocolLibEntity): McProtocolLibEntity {
        return entities.getOrPut(id, factory)
    }

    fun get(id: UUID): McProtocolLibEntity? = entities[id]

    fun remove(id: UUID) {
        entities.remove(id)
    }

    fun allEntities(): Collection<McProtocolLibEntity> = entities.values
}
