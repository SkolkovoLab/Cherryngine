package ru.cherryngine.lib.minecraft.tide.stream

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils

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