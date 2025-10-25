package io.github.dockyardmc.codec

import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import kotlin.reflect.KClass

class ActionStreamCodec<T : Any>(
    val indexCodec: StreamCodec<Int>,
    vararg val actions: Entry<out T>
) : StreamCodec<T> {
    override fun write(buffer: ByteBuf, value: T) {
        val kClass = value::class
        val index = actions.indexOfFirst { it.kClass == kClass }
        if (index < 0) throw NoSuchElementException()
        indexCodec.write(buffer, index)
        @Suppress("UNCHECKED_CAST")
        val codec = actions[index].streamCodec as StreamCodec<T>
        codec.write(buffer, value)
    }

    override fun read(buffer: ByteBuf): T {
        val index = indexCodec.read(buffer)
        val codec = actions[index].streamCodec
        return codec.read(buffer)
    }

    data class Entry<T : Any>(
        val kClass: KClass<T>,
        val streamCodec: StreamCodec<T>
    )
}