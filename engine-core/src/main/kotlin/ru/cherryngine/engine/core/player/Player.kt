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

    val clientPosition: Vec3D
    val clientYawPitch: YawPitch

    fun teleport(position: Vec3D, yawPitch: YawPitch)
    fun setVelocity(velocity: Vec3D)

    override fun sendMessage(message: Component)
}
