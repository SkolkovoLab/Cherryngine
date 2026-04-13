package ru.cherryngine.engine.mcprotocollib

class McProtocolLibEntityRegistry {
    private val entities = mutableSetOf<McProtocolLibEntity>()

    fun add(entity: McProtocolLibEntity) { entities.add(entity) }
    fun remove(entity: McProtocolLibEntity) { entities.remove(entity) }
    fun allEntities(): Collection<McProtocolLibEntity> = entities
}
