package ru.cherryngine.lib.minecraft.codec

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import kotlin.reflect.KClass

class ActionStreamCodec<T : Any>(
    val indexCodec: StreamCodec<Int>,
    vararg val actions: IEntry<out T>,
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

    sealed interface IEntry<T : Any> {
        val kClass: KClass<T>?
        val streamCodec: StreamCodec<T>
    }

    data class Entry<T : Any>(
        override val kClass: KClass<T>,
        override val streamCodec: StreamCodec<T>,
    ) : IEntry<T>

    class Skip<T : Any> : IEntry<T> {
        override val kClass: KClass<T>? get() = null
        override val streamCodec: StreamCodec<T> get() = throw IllegalStateException()
    }
}