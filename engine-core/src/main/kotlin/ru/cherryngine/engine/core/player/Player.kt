package ru.cherryngine.engine.core.player

import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

interface Player : CommandSender {
    val uuid: UUID
    val username: String
    val viewContextIDs: Set<String>

    /** Абсолютный телепорт: position + yawPitch применяются как есть, камера клиента поворачивается. */
    fun teleport(position: Vec3D, yawPitch: YawPitch)

    /**
     * Мягко переместить клиента в [position] без смены направления взгляда и без модификации velocity.
     * Каждая платформа выбирает оптимальный путь (Java Edition — relative teleport через RelativeFlags.ALL,
     * Bedrock — absolute teleport с сохранённым clientYawPitch).
     */
    fun correctClientPosition(position: Vec3D)

    fun setVelocity(velocity: Vec3D)

    override fun sendMessage(message: Component)
}
