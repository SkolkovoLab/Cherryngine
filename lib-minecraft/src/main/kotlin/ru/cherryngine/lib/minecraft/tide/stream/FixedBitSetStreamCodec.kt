package ru.cherryngine.lib.minecraft.tide.stream

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils.toByteArraySafe
import java.util.*

class FixedBitSetStreamCodec(
    val length: Int
) : StreamCodec<BitSet> {
    override fun write(buffer: ByteBuf, value: BitSet) {
        val setLength = value.length()
        if (setLength > length) throw Codec.EncodingException("BitSet is larger than expected size ($setLength > $length)")
        StreamCodec.RAW_BYTES_ARRAY.write(buffer, value.toByteArray())
    }

    override fun read(buffer: ByteBuf): BitSet {
        val array = buffer.readBytes((length + 7) / 8)
        return BitSet.valueOf(array.toByteArraySafe())
    }

}