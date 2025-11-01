package ru.cherryngine.lib.minecraft.world

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

class MutableLight(
    var skyMask: BitSet = BitSet(),
    var blockMask: BitSet = BitSet(),
    var emptySkyMask: BitSet = BitSet(),
    var emptyBlockMask: BitSet = BitSet(),
    var skyLight: List<ByteArray> = emptyList(),
    var blockLight: List<ByteArray> = emptyList()
) {
    init {

    }
    companion object {
        val EMPTY = MutableLight()

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BIT_SET, MutableLight::skyMask,
            StreamCodec.BIT_SET, MutableLight::blockMask,
            StreamCodec.BIT_SET, MutableLight::emptySkyMask,
            StreamCodec.BIT_SET, MutableLight::emptyBlockMask,
            StreamCodec.BYTE_ARRAY.list(), MutableLight::skyLight,
            StreamCodec.BYTE_ARRAY.list(), MutableLight::blockLight,
            ::MutableLight
        )
    }
}
