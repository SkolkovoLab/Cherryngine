package io.github.dockyardmc.tide.stream

import io.github.dockyardmc.tide.codec.CodecUtils
import io.netty.buffer.ByteBuf

class StringStreamCodec(
    val maxSize: Int = Short.MAX_VALUE.toInt()
) : StreamCodec<String> {
    override fun write(buffer: ByteBuf, value: String) {
        CodecUtils.writeString(buffer, value)
    }

    override fun read(buffer: ByteBuf): String {
        return CodecUtils.readString(buffer, maxSize)
    }
}