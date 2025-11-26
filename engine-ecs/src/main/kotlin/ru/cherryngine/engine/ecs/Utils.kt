package ru.cherryngine.engine.ecs

import ru.cherryngine.engine.ecs.components.PlayerComponent
import java.util.*

fun EcsWorld.getPlayerEntity(playerUuid: UUID): EcsEntity {
    return family { all(PlayerComponent) }.firstOrNull {
        it[PlayerComponent].uuid == playerUuid
    } ?: throw RuntimeException("Entity for playerUUID $playerUuid doesn't exist!")
}