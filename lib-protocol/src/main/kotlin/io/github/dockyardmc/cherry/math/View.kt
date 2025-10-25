package io.github.dockyardmc.cherry.math

import io.github.dockyardmc.cherry.math.rotation.QRot
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
        QRot.Companion.fromAxisAngle(Vec3D.Companion.MINUS_Y, Math.toRadians(yaw.toDouble()))

    fun getPitchRotation(): QRot =
        QRot.Companion.fromAxisAngle(Vec3D.Companion.PLUS_X, Math.toRadians(pitch.toDouble()))

    fun getRotation(): QRot {
        if (yaw == 0f && pitch == 0f) return QRot.Companion.IDENTITY
        if (yaw == 0f) return getPitchRotation()
        if (pitch == 0f) return getYawRotation()
        return getYawRotation() * getPitchRotation()
    }
}
