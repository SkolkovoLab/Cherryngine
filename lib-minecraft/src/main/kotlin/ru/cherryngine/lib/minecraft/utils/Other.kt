package ru.cherryngine.lib.minecraft.utils

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


fun UUID.toIntArray(): IntArray {
    return uuidToIntArray(this)
}

fun uuidToIntArray(uuid: UUID): IntArray {
    val uuidMost = uuid.mostSignificantBits
    val uuidLeast = uuid.leastSignificantBits
    return intArrayOf(
        (uuidMost shr 32).toInt(),
        uuidMost.toInt(),
        (uuidLeast shr 32).toInt(),
        uuidLeast.toInt()
    )
}

fun clamp(value: Double, min: Short, max: Short): Int {
    return min(max(value, min.toDouble()), max.toDouble()).toInt()
}

fun bitsToRepresent(n: Int): Int {
    require(n > 0) { "n must be greater than 0" }
    return Int.SIZE_BITS - n.countLeadingZeroBits()
}

fun String.toComponent(): Component = Component.text(this)

fun Component.toNBT() = NBTComponentSerializer.nbt().serialize(this) as CompoundBinaryTag

fun Vec3D.blockPos(): Vec3I {
    return Vec3I(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())
}

inline fun <R> ByteBuf.use(block: (ByteBuf) -> R): R {
    try {
        return block(this)
    } catch (e: Throwable) {
        throw e
    } finally {
        release()
    }
}