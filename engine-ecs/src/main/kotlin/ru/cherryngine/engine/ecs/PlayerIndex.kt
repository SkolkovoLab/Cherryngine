package ru.cherryngine.engine.ecs

import java.util.*

class PlayerIndex {
    private val index = HashMap<UUID, EcsEntity>()

    fun onAdd(entity: EcsEntity, uuid: UUID) {
        index[uuid] = entity
    }

    fun onRemove(uuid: UUID) {
        index.remove(uuid)
    }

    fun get(uuid: UUID): EcsEntity? = index[uuid]

    fun getOrThrow(uuid: UUID): EcsEntity =
        index[uuid] ?: error("Entity for playerUUID $uuid doesn't exist!")
}
