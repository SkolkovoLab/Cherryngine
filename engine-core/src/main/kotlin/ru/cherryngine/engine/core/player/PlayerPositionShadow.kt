package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@InstanceSingleton
class PlayerPositionShadow {
    private val map = ConcurrentHashMap<UUID, MovementSnapshot>()

    operator fun get(uuid: UUID): MovementSnapshot? = map[uuid]
    operator fun set(uuid: UUID, snapshot: MovementSnapshot) {
        map[uuid] = snapshot
    }

    fun remove(uuid: UUID) {
        map.remove(uuid)
    }
}
