package ru.cherryngine.engine.core.entity.api

import net.minestom.server.entity.EntityType
import ru.cherryngine.engine.core.entity.EngineEntity

fun entity(type: EntityType, data: Int = 0, init: EngineEntity.() -> Unit): EngineEntity {
    val entity = EngineEntity(type, data)
    entity.init()
    return entity
}