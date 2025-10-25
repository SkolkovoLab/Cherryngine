package io.github.dockyardmc.utils

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.protocol.types.SoundEvent
import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer
import java.util.*
import kotlin.experimental.and
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


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

    return Integer.SIZE - Integer.numberOfLeadingZeros(n)
}

fun String.toComponent(): Component = Component.text(this)

fun Component.toNBT() = NBTComponentSerializer.nbt().serialize(this) as CompoundBinaryTag
