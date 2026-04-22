package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@InstanceSingleton
class PlayerPositionShadow {
    private val map = ConcurrentHashMap<UUID, PositionSnapshot>()

    operator fun get(uuid: UUID): PositionSnapshot? = map[uuid]
    operator fun set(uuid: UUID, snapshot: PositionSnapshot) {
        map[uuid] = snapshot
    }

    fun remove(uuid: UUID) {
        map.remove(uuid)
    }
}
