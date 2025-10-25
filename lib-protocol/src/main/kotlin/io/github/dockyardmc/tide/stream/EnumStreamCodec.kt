package io.github.dockyardmc.tide.stream

import io.netty.buffer.ByteBuf
import kotlin.reflect.KClass

class EnumStreamCodec<E : Enum<E>>(
    val kClass: KClass<E>
) : StreamCodec<E> {
    override fun write(buffer: ByteBuf, value: E) {
        StreamCodec.VAR_INT.write(buffer, value.ordinal)
    }

    override fun read(buffer: ByteBuf): E {
        return kClass.java.enumConstants[StreamCodec.VAR_INT.read(buffer)]
    }

    companion object {
        inline operator fun <reified E : Enum<E>> invoke(): StreamCodec<E> {
            return EnumStreamCodec<E>(E::class)
        }
    }
}