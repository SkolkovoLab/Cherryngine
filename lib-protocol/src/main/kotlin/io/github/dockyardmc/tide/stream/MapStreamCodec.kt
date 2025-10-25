package io.github.dockyardmc.tide.stream

import io.netty.buffer.ByteBuf

class MapStreamCodec<K, V>(
    val keyCodec: StreamCodec<K>,
    val valueCodec: StreamCodec<V>
) : StreamCodec<Map<K, V>> {
    override fun write(buffer: ByteBuf, value: Map<K, V>) {
        StreamCodec.VAR_INT.write(buffer, value.size)
        value.forEach { (key, value) ->
            keyCodec.write(buffer, key)
            valueCodec.write(buffer, value)
        }
    }

    override fun read(buffer: ByteBuf): Map<K, V> {
        val size = StreamCodec.VAR_INT.read(buffer)
        val map = mutableMapOf<K, V>()
        for (i in 0 until size) {
            val key = keyCodec.read(buffer)
            val value = valueCodec.read(buffer)
            map[key] = value
        }

        return map
    }
}