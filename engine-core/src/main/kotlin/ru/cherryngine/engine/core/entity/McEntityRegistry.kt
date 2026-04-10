package ru.cherryngine.engine.core.entity

import java.util.*

class McEntityRegistry {
    private val entities = HashMap<UUID, McEntity>()
    private val seenThisTick = HashSet<UUID>()

    fun getOrCreate(mcEntityId: UUID, factory: () -> McEntity): McEntity {
        return entities.getOrPut(mcEntityId, factory)
    }

    fun keepAlive(mcEntityId: UUID) {
        seenThisTick.add(mcEntityId)
    }

    fun beginTick() {
        seenThisTick.clear()
    }

    fun endTick(onRemove: (McEntity) -> Unit) {
        val toRemove = entities.keys.filter { it !in seenThisTick }
        toRemove.forEach { uuid ->
            val entity = entities.remove(uuid)!!
            onRemove(entity)
        }
    }
}
