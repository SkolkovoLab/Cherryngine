package ru.cherryngine.platform.minecraft.java.player

import jakarta.inject.Singleton
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Кеш "что мы знаем про клиента": позиция, поворот, onGround.
 * Пишется и из входящих движений (ConnectionService.onMove), и из исходящих
 * teleport/correctClientPosition — последний вариант даёт оптимистичный апдейт
 * без которого PostSyncTickable бы спамил re-teleport до подтверждения клиента.
 *
 * Заменяет соответствующие поля на MinecraftPlayer — сам Player хранит только
 * connection и rawPackets-снепшот, derived-состояние выехало в этот side-bag.
 */
@Singleton
class MinecraftClientState {
    private val positions = ConcurrentHashMap<UUID, Vec3D>()
    private val yawPitches = ConcurrentHashMap<UUID, YawPitch>()
    private val onGround = ConcurrentHashMap<UUID, Boolean>()

    fun position(uuid: UUID): Vec3D? = positions[uuid]
    fun yawPitch(uuid: UUID): YawPitch? = yawPitches[uuid]
    fun isOnGround(uuid: UUID): Boolean = onGround[uuid] ?: false

    fun setPosition(uuid: UUID, pos: Vec3D) {
        positions[uuid] = pos
    }

    fun setYawPitch(uuid: UUID, yp: YawPitch) {
        yawPitches[uuid] = yp
    }

    fun setOnGround(uuid: UUID, og: Boolean) {
        onGround[uuid] = og
    }

    fun forget(uuid: UUID) {
        positions.remove(uuid)
        yawPitches.remove(uuid)
        onGround.remove(uuid)
    }
}
