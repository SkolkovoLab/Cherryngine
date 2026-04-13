package ru.cherryngine.engine.core.player

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

interface PlayerOutputProvider {
    fun teleport(uuid: UUID, position: Vec3D, yawPitch: YawPitch)
    fun sendMessage(uuid: UUID, message: Component)
    fun setVelocity(uuid: UUID, velocity: Vec3D)
}
