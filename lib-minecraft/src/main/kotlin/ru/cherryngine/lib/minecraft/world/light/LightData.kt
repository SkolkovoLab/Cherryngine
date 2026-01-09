package ru.cherryngine.lib.minecraft.world.light

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import java.util.*

class LightData(
    val skyMask: BitSet = BitSet(),
    val blockMask: BitSet = BitSet(),
    val emptySkyMask: BitSet = BitSet(),
    val emptyBlockMask: BitSet = BitSet(),
    val skyLight: List<ByteArray> = emptyList(),
    val blockLight: List<ByteArray> = emptyList()
) {
    companion object {
        val EMPTY = LightData() // FIXME оно должно быть иммутабельным

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BIT_SET, LightData::skyMask,
            StreamCodec.BIT_SET, LightData::blockMask,
            StreamCodec.BIT_SET, LightData::emptySkyMask,
            StreamCodec.BIT_SET, LightData::emptyBlockMask,
            StreamCodec.BYTE_ARRAY.list(), LightData::skyLight,
            StreamCodec.BYTE_ARRAY.list(), LightData::blockLight,
            ::LightData
        )
    }
}