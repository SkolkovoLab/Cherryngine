package ru.cherryngine.lib.minecraft.tide.stream

import io.netty.buffer.ByteBuf

data class UnionStreamCodec<T, K, out TR : T>(
    val keyCodec: StreamCodec<K>,
    val keyFunc: (T) -> K,
    val serializers: (K) -> StreamCodec<out TR>
) : StreamCodec<T> {
    @Suppress("UNCHECKED_CAST")
    override fun write(buffer: ByteBuf, value: T) {
        val key = keyFunc.invoke(value)
        keyCodec.write(buffer, key)
        val serializer = serializers.invoke(key)
        (serializer as StreamCodec<TR>).write(buffer, value as TR)
    }

    override fun read(buffer: ByteBuf): T {
        val key = keyCodec.read(buffer)
        val serializer = serializers.invoke(key)
        return serializer.read(buffer)
    }
}