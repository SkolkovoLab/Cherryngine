package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.ecs.components.PlayerComponent
import java.util.UUID

fun EcsWorld.getPlayerEntity(playerUuid: UUID): EcsEntity {
    return getPlayerEntityOrNull(playerUuid)
        ?: throw RuntimeException("Entity for playerUUID $playerUuid doesn't exist!")
}

fun EcsWorld.getPlayerEntityOrNull(playerUuid: UUID): EcsEntity? {
    return family { all(PlayerComponent) }.firstOrNull {
        it[PlayerComponent].uuid == playerUuid
    }
}