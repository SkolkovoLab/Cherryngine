package ru.cherryngine.lib.minecraft.world

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

/**
 * Он по задумке имутабельный, пж не меняйте ничего
 */
class Light(
    val skyMask: BitSet = BitSet(),
    val blockMask: BitSet = BitSet(),
    val emptySkyMask: BitSet = BitSet(),
    val emptyBlockMask: BitSet = BitSet(),
    val skyLight: List<ByteArray> = emptyList(),
    val blockLight: List<ByteArray> = emptyList()
) {
    companion object {
        val EMPTY = Light()

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
