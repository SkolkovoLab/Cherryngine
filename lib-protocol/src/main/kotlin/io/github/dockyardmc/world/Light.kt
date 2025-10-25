package io.github.dockyardmc.world

import io.github.dockyardmc.tide.stream.StreamCodec
import java.util.*

data class Light(
    var skyMask: BitSet = BitSet(),
    var blockMask: BitSet = BitSet(),
    var emptySkyMask: BitSet = BitSet(),
    var emptyBlockMask: BitSet = BitSet(),
    var skyLight: List<ByteArray> = emptyList(),
    var blockLight: List<ByteArray> = emptyList()
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BIT_SET, Light::skyMask,
            StreamCodec.BIT_SET, Light::blockMask,
            StreamCodec.BIT_SET, Light::emptySkyMask,
            StreamCodec.BIT_SET, Light::emptyBlockMask,
            StreamCodec.BYTE_ARRAY.list(), Light::skyLight,
            StreamCodec.BYTE_ARRAY.list(), Light::blockLight,
            ::Light
        )
    }
}
