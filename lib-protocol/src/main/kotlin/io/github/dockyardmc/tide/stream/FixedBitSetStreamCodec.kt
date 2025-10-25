package io.github.dockyardmc.tide.stream

import io.github.dockyardmc.tide.codec.Codec
import io.github.dockyardmc.tide.codec.CodecUtils.toByteArraySafe
import io.netty.buffer.ByteBuf
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