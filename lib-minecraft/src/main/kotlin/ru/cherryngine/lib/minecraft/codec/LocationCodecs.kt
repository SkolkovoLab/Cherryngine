package ru.cherryngine.lib.minecraft.codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.math.rotation.QRot
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.clamp
import kotlin.math.roundToInt

object LocationCodecs {
    val BLOCK_POSITION = StreamCodec.LONG.transform<Vec3I>(
        { to ->
            val x = (to shr 38).toInt()
            val y = (to shl 52 shr 52).toInt()
            val z = (to shl 26 shr 38).toInt()
            Vec3I(x, y, z)
        }
    ) { from ->
        val blockX = from.x.toLong()
        val blockY = from.y.toLong()
        val blockZ = from.z.toLong()
        (blockX and 0x3FFFFFF shl 38) or ((blockZ and 0x3FFFFFF) shl 12) or (blockY and 0xFFF)
    }

    val VEC_3D: StreamCodec<Vec3D> = StreamCodec.of(
        StreamCodec.DOUBLE, Vec3D::x,
        StreamCodec.DOUBLE, Vec3D::y,
        StreamCodec.DOUBLE, Vec3D::z,
        ::Vec3D
    )

    val VEC_3D_FLOAT: StreamCodec<Vec3D> = StreamCodec.of(
        StreamCodec.DOUBLE_FLOAT, Vec3D::x,
        StreamCodec.DOUBLE_FLOAT, Vec3D::y,
        StreamCodec.DOUBLE_FLOAT, Vec3D::z,
        ::Vec3D
    )

    val YAW_PITCH = StreamCodec.of(
        StreamCodec.FLOAT, YawPitch::yaw,
        StreamCodec.FLOAT, YawPitch::pitch,
        ::YawPitch
    )

    val ANGLE = object : StreamCodec<Float> {
        override fun write(buffer: ByteBuf, value: Float) {
            buffer.writeByte(((value % 360) * 256 / 360).toInt())
        }

        override fun read(buffer: ByteBuf): Float {
            return (buffer.readByte().toInt() * 360 / 256.0f) % 360
        }
    }

    val ANGLE_PITCH_YAW = StreamCodec.of(
        ANGLE, YawPitch::pitch,
        ANGLE, YawPitch::yaw
    ) { pitch, yaw -> YawPitch(yaw, pitch) }

    val MOVE_ENTITY_DELTA = object : StreamCodec<Vec3D> {
        override fun write(buffer: ByteBuf, value: Vec3D) {
            buffer.writeShort((value.x * 4096).roundToInt())
            buffer.writeShort((value.y * 4096).roundToInt())
            buffer.writeShort((value.z * 4096).roundToInt())
        }

        override fun read(buffer: ByteBuf): Vec3D {
            val x = buffer.readShort().toInt() / 4096.0
            val y = buffer.readShort().toInt() / 4096.0
            val z = buffer.readShort().toInt() / 4096.0
            return Vec3D(x, y, z)
        }
    }

    val VELOCITY = object : StreamCodec<Vec3D> {
        override fun write(buffer: ByteBuf, value: Vec3D) {
            buffer.writeShort(clamp(value.x, Short.MIN_VALUE, Short.MAX_VALUE))
            buffer.writeShort(clamp(value.y, Short.MIN_VALUE, Short.MAX_VALUE))
            buffer.writeShort(clamp(value.z, Short.MIN_VALUE, Short.MAX_VALUE))
        }

        override fun read(buffer: ByteBuf): Vec3D {
            val x = buffer.readShort().toDouble()
            val y = buffer.readShort().toDouble()
            val z = buffer.readShort().toDouble()
            return Vec3D(x, y, z)
        }
    }

    val SOUND_LOCATION = object : StreamCodec<Vec3D> {
        override fun write(buffer: ByteBuf, value: Vec3D) {
            buffer.writeInt((value.x * 8.0).roundToInt())
            buffer.writeInt((value.y * 8.0).roundToInt())
            buffer.writeInt((value.z * 8.0).roundToInt())
        }

        override fun read(buffer: ByteBuf): Vec3D {
            val x = buffer.readInt() / 8.0
            val y = buffer.readInt() / 8.0
            val z = buffer.readInt() / 8.0
            return Vec3D(x, y, z)
        }
    }

    val QUATERNION = object : StreamCodec<QRot> {
        override fun write(buffer: ByteBuf, value: QRot) {
            buffer.writeFloat(value.x.toFloat())
            buffer.writeFloat(value.y.toFloat())
            buffer.writeFloat(value.z.toFloat())
            buffer.writeFloat(value.w.toFloat())
        }

        override fun read(buffer: ByteBuf): QRot {
            val x = buffer.readFloat().toDouble()
            val y = buffer.readFloat().toDouble()
            val z = buffer.readFloat().toDouble()
            val w = buffer.readFloat().toDouble()
            return QRot(w, x, y, z)
        }
    }
}