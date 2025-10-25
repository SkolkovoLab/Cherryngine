package io.github.dockyardmc.cherry.math.rotation

import io.github.dockyardmc.cherry.math.Vec3D
import kotlin.math.*

data class QRot(
    val w: Double,
    val x: Double,
    val y: Double,
    val z: Double,
) {
    companion object {
        private const val EPSILON = 0.000001
        private val SAFE_MIN = java.lang.Double.longBitsToDouble(4503599627370496L)
        private const val MAX_DOT_THRESHOLD = 0.9995

        val IDENTITY = QRot(1.0, 0.0, 0.0, 0.0)

        /**
         * Безопасный способ создания QRot
         * Создавая через конструктор можно прокинуть любое говно, а через of оно сразу нормализует
         * */
        fun of(w: Double, x: Double, y: Double, z: Double): QRot {
            val normSquared = w * w + x * x + y * y + z * z
            val norm = sqrt(normSquared)
            if (norm < SAFE_MIN || !norm.isFinite()) {
                throw IllegalStateException("Illegal norm: $norm")
            }

            return if (w >= 0.0) {
                QRot(w / norm, x / norm, y / norm, z / norm)
            } else {
                QRot(-w / norm, -x / norm, -y / norm, -z / norm)
            }
        }

        fun fromAxisAngle(axis: Vec3D, angle: Double): QRot {
            val normAxis = axis.normalize()
            require(angle.isFinite()) { "Invalid angle: $angle" }
            val halfAngle = 0.5 * angle
            val sinHalfAngle = sin(halfAngle)
            val w = cos(halfAngle)
            val x = sinHalfAngle * normAxis.x
            val y = sinHalfAngle * normAxis.y
            val z = sinHalfAngle * normAxis.z
            return QRot(w, x, y, z)
        }

        fun fromAxisAngleSequence(sequence: AxisAngleSequence): QRot {
            val axes = sequence.axisSequence
            val q1 = fromAxisAngle(axes.axis1, sequence.angle1)
            val q2 = fromAxisAngle(axes.axis2, sequence.angle2)
            val q3 = fromAxisAngle(axes.axis3, sequence.angle3)
            return q1.times(q2).times(q3)
        }
    }

    init {
        val normSquared = w * w + x * x + y * y + z * z
        require(abs(normSquared - 1) < EPSILON)
    }

    operator fun times(other: QRot): QRot {
        val q1a = w
        val q1b = x
        val q1c = y
        val q1d = z
        val q2a = other.w
        val q2b = other.x
        val q2c = other.y
        val q2d = other.z
        val w = q1a * q2a - q1b * q2b - q1c * q2c - q1d * q2d
        val x = q1a * q2b + q1b * q2a + q1c * q2d - q1d * q2c
        val y = q1a * q2c - q1b * q2d + q1c * q2a + q1d * q2b
        val z = q1a * q2d + q1b * q2c - q1c * q2b + q1d * q2a
        return QRot(w, x, y, z)
    }

    operator fun div(other: QRot): QRot =
        this * other.inverse()

    fun inverse(): QRot = QRot(w, -x, -y, -z)

    fun dot(other: QRot): Double = w * other.w + x * other.x + y * other.y + z * other.z

    fun slerp(other: QRot, alpha: Double): QRot {
        var dot = dot(other)
        var endW: Double = other.w
        var endX: Double = other.x
        var endY: Double = other.y
        var endZ: Double = other.z

        if (dot < 0.0) {
            dot = -dot
            endW = -endW
            endX = -endX
            endY = -endY
            endZ = -endZ
        }

        return if (dot > MAX_DOT_THRESHOLD) {
            val f = 1.0 - alpha
            of(
                f * w + alpha * endW,
                f * x + alpha * endX,
                f * y + alpha * endY,
                f * z + alpha * endZ
            )
        } else {
            val theta = acos(dot)
            val sinTheta = sin(theta)
            val f1 = sin((1.0 - alpha) * theta) / sinTheta
            val f2 = sin(alpha * theta) / sinTheta
            of(
                f1 * w + f2 * endW,
                f1 * x + f2 * endX,
                f1 * y + f2 * endY,
                f1 * z + f2 * endZ
            )
        }
    }

    fun toAxisAngleSequence(axes: AxisSequence): AxisAngleSequence {
        return when (axes.type) {
            AxisSequence.Type.TAIT_BRYAN -> getRelativeTaitBryanAngles(axes)
            AxisSequence.Type.EULER -> getRelativeEulerAngles(axes)
        }
    }

    fun apply(vec: Vec3D): Vec3D {
        val vx: Double = vec.x
        val vy: Double = vec.y
        val vz: Double = vec.z
        val iw = -(x * vx) - y * vy - z * vz
        val ix = w * vx + y * vz - z * vy
        val iy = w * vy + z * vx - x * vz
        val iz = w * vz + x * vy - y * vx
        return Vec3D(
            iw * -x + ix * w + iy * -z - iz * -y,
            iw * -y - ix * -z + iy * w + iz * -x,
            iw * -z + ix * -y - iy * -x + iz * w
        )
    }

    //region private shit
    private fun getRelativeTaitBryanAngles(axes: AxisSequence): AxisAngleSequence {
        val axis1 = axes.axis1
        val axis2 = axes.axis2
        val axis3 = axes.axis3
        val vec3: Vec3D = axis3.rotate(this)
        val invVec1: Vec3D = axis1.rotate(this.inverse())
        val angle2Sin: Double = vec3.dot(axis2.cross(axis3))
        if (!(angle2Sin < -0.9999999999) && !(angle2Sin > 0.9999999999)) {
            val crossAxis13: Vec3D = axis1.cross(axis3)
            val angle1TanY: Double = vec3.dot(crossAxis13)
            val angle1TanX: Double = vec3.dot(axis3)
            val angle3TanY: Double = invVec1.dot(crossAxis13)
            val angle3TanX: Double = invVec1.dot(axis1)
            return AxisAngleSequence(
                axes,
                atan2(angle1TanY, angle1TanX),
                asin(angle2Sin),
                atan2(angle3TanY, angle3TanX)
            )
        } else {
            val vec2: Vec3D = axis2.rotate(this)
            val angle1TanY: Double = vec2.dot(axis1.cross(axis2))
            val angle1TanX: Double = vec2.dot(axis2)
            val angle2 = if (angle2Sin > 0.9999999999) (Math.PI / 2.0) else (-Math.PI / 2.0)
            return AxisAngleSequence(axes, atan2(angle1TanY, angle1TanX), angle2, 0.0)
        }
    }

    private fun getRelativeEulerAngles(axes: AxisSequence): AxisAngleSequence {
        val axis1 = axes.axis1
        val axis2 = axes.axis2
        val crossAxis: Vec3D = axis1.cross(axis2)
        val vec1: Vec3D = axis1.rotate(this)
        val invVec1: Vec3D = axis1.rotate(this.inverse())
        val angle2Cos: Double = vec1.dot(axis1)
        if (!(angle2Cos < -0.9999999999) && !(angle2Cos > 0.9999999999)) {
            val angle1TanY: Double = vec1.dot(axis2)
            val angle1TanX: Double = -vec1.dot(crossAxis)
            val angle3TanY: Double = invVec1.dot(axis2)
            val angle3TanX: Double = invVec1.dot(crossAxis)
            return AxisAngleSequence(
                axes,
                atan2(angle1TanY, angle1TanX),
                acos(angle2Cos),
                atan2(angle3TanY, angle3TanX)
            )
        } else {
            val vec2: Vec3D = axis2.rotate(this)
            val angle1TanY: Double = vec2.dot(crossAxis)
            val angle1TanX: Double = vec2.dot(axis2)
            val angle2 = if (angle2Cos > 0.9999999999) 0.0 else Math.PI
            return AxisAngleSequence(axes, atan2(angle1TanY, angle1TanX), angle2, 0.0)
        }
    }
    //endregion
}