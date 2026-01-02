package ru.cherryngine.lib.minecraft.network.stream_codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.math.Vec3D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToLong

object LpVec3StreamCodec : StreamCodec<Vec3D> {
    private const val DATA_BITS = 15
    private const val DATA_BITS_MASK = 0x7FFF // 32767
    private const val MAX_QUANTIZED_VALUE = 32766.0

    private const val SCALE_BITS = 2
    private const val SCALE_BITS_MASK = 0x3 // 3

    private const val CONTINUATION_FLAG = 0x4 // 4

    private const val X_OFFSET = 3
    private const val Y_OFFSET = 18
    private const val Z_OFFSET = 33

    const val ABS_MAX_VALUE = 1.7179869183E10
    const val ABS_MIN_VALUE = 3.051944088384301E-5

    fun hasContinuationBit(`in`: Int): Boolean {
        return (`in` and CONTINUATION_FLAG) == CONTINUATION_FLAG
    }

    override fun read(input: ByteBuf): Vec3D {
        val lowest = input.readUnsignedByte().toInt()
        if (lowest == 0) return Vec3D.Companion.ZERO

        val middle = input.readUnsignedByte().toInt()
        val highest = input.readUnsignedInt()

        val buffer = (highest shl 16) or (middle.toLong() shl 8) or lowest.toLong()

        var scale = (lowest and SCALE_BITS_MASK).toLong()
        if (hasContinuationBit(lowest)) {
            // Keep the Java semantics: (VarInt.read(input) & 0xFFFFFFFFL) << 2
            scale = scale or ((StreamCodec.VAR_INT.read(input).toLong() and 0xFFFF_FFFFL) shl 2)
        }

        return Vec3D(
            unpack(buffer shr X_OFFSET) * scale,
            unpack(buffer shr Y_OFFSET) * scale,
            unpack(buffer shr Z_OFFSET) * scale
        )
    }

    override fun write(output: ByteBuf, value: Vec3D) {
        val x = sanitize(value.x)
        val y = sanitize(value.y)
        val z = sanitize(value.z)

        val chessboardLength = absMax(x, absMax(y, z))
        if (chessboardLength < ABS_MIN_VALUE) {
            output.writeByte(0)
            return
        }

        val scale = ceilLong(chessboardLength)
        val isPartial = (scale and 3L) != scale
        val markers = if (isPartial) (scale and 3L) or CONTINUATION_FLAG.toLong() else scale

        val xn = pack(x / scale) shl X_OFFSET
        val yn = pack(y / scale) shl Y_OFFSET
        val zn = pack(z / scale) shl Z_OFFSET

        val buffer = markers or xn or yn or zn

        output.writeByte(buffer.toInt())
        output.writeByte((buffer shr 8).toInt())
        output.writeInt((buffer shr 16).toInt())

        if (isPartial) {
            StreamCodec.VAR_INT.write(output, (scale shr 2).toInt())
        }
    }

    private fun sanitize(value: Double): Double {
        if (value.isNaN()) return 0.0
        return value.coerceIn(-ABS_MAX_VALUE, ABS_MAX_VALUE)
    }

    private fun pack(value: Double): Long {
        return (((value * 0.5 + 0.5) * MAX_QUANTIZED_VALUE).roundToLong())
    }

    private fun unpack(value: Long): Double {
        val q = min((value and DATA_BITS_MASK.toLong()).toDouble(), MAX_QUANTIZED_VALUE)
        return q * 2.0 / MAX_QUANTIZED_VALUE - 1.0
    }

    private fun absMax(a: Double, b: Double): Double {
        return max(abs(a), abs(b))
    }

    private fun ceilLong(v: Double): Long {
        val l = v.toLong()
        return if (v > l) l + 1L else l
    }
}