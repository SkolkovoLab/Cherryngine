package ru.cherryngine.engine.core

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.UUID

interface PlayerOutputProvider {
    fun teleport(uuid: UUID, position: Vec3D, yawPitch: YawPitch)
    fun sendMessage(uuid: UUID, message: Component)
}
