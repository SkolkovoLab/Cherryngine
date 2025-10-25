package io.github.dockyardmc.data

import io.github.dockyardmc.cherry.math.Vec3I
import io.github.dockyardmc.tide.codec.Codec
import io.github.dockyardmc.tide.stream.StreamCodec

object NetworkVec3I {
    val CODEC = Codec.INT_ARRAY.transform({ from -> from.toVector3() }, { to -> to.toIntArray() })

    val STREAM_CODEC = StreamCodec.of(
        StreamCodec.VAR_INT, Vec3I::x,
        StreamCodec.VAR_INT, Vec3I::y,
        StreamCodec.VAR_INT, Vec3I::z,
        ::Vec3I
    )

    fun IntArray.toVector3(): Vec3I {
        require(this.size == 3) { "IntArray must have exactly 3 elements" }
        return Vec3I(this[0], this[1], this[2])
    }

    fun Vec3I.toIntArray(): IntArray {
        return intArrayOf(x, y, z)
    }
}