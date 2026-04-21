package ru.cherryngine.platform.minecraft.java.integration.grim.utils

import ac.grim.grimac.platform.api.world.PlatformWorld
import ac.grim.grimac.utils.math.Location
import com.github.retrooper.packetevents.util.Vector3d
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch


fun Vec3D.packeteventsVector3d(): Vector3d = Vector3d(x, y, z)

fun Vector3d.vec3D(): Vec3D = Vec3D(x, y, z)

fun Vec3D.grimLocation(world: PlatformWorld, yawPitch: YawPitch = YawPitch.ZERO): Location =
    Location(world, x, y, z, yawPitch.yaw, yawPitch.pitch)

fun Location.vec3D(): Vec3D = Vec3D(x, y, z)
fun Location.yawPitch(): YawPitch = YawPitch(yaw, pitch)
