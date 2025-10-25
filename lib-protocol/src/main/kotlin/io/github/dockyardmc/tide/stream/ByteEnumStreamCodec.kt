package io.github.dockyardmc.tide.stream

import io.netty.buffer.ByteBuf
import kotlin.reflect.KClass

class ByteEnumStreamCodec<E : Enum<E>>(
    val kClass: KClass<E>
) : StreamCodec<E> {
    override fun write(buffer: ByteBuf, value: E) {
        StreamCodec.BYTE.write(buffer, value.ordinal.toByte())
    }

    override fun read(buffer: ByteBuf): E {
        return kClass.java.enumConstants[StreamCodec.BYTE.read(buffer).toInt()]
    }

    companion object {
        inline operator fun <reified E : Enum<E>> invoke(): StreamCodec<E> {
            return ByteEnumStreamCodec<E>(E::class)
        }
    }
}