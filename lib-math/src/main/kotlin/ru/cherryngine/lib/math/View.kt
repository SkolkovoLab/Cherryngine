package ru.cherryngine.lib.math

import ru.cherryngine.lib.math.rotation.QRot
import kotlin.math.cos
import kotlin.math.sin

data class View(
    val yaw: Float,
    val pitch: Float,
) {
    companion object {
        val ZERO: View = View(0f, 0f)
    }

    val yawRadians: Double get() = Math.toRadians(yaw.toDouble())
    val pitchRadians: Double get() = Math.toRadians(pitch.toDouble())

    operator fun plus(other: View): View {
        return View(yaw + other.yaw, pitch + other.pitch)
    }

    operator fun minus(other: View): View {
        return View(yaw - other.yaw, pitch - other.pitch)
    }

    operator fun times(value: Float): View {
        return View(yaw * value, pitch * value)
    }

    operator fun times(value: View): View {
        return View(yaw * value.yaw, pitch * value.pitch)
    }

    operator fun div(value: Float): View {
        return View(yaw / value, pitch / value)
    }

    fun direction(): Vec3D {
        val rotX = yawRadians
        val rotY = pitchRadians
        val xz = cos(rotY)
        return Vec3D(-xz * sin(rotX), -sin(rotY), xz * cos(rotX))
    }

    fun getYawRotation(): QRot =
        QRot.fromAxisAngle(Vec3D.MINUS_Y, Math.toRadians(yaw.toDouble()))

    fun getPitchRotation(): QRot =
        QRot.fromAxisAngle(Vec3D.PLUS_X, Math.toRadians(pitch.toDouble()))

    fun getRotation(): QRot {
        if (yaw == 0f && pitch == 0f) return QRot.IDENTITY
        if (yaw == 0f) return getPitchRotation()
        if (pitch == 0f) return getYawRotation()
        return getYawRotation() * getPitchRotation()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is View -> other.yaw == yaw && other.pitch == pitch
            else -> super.equals(other)
        }
    }
}
