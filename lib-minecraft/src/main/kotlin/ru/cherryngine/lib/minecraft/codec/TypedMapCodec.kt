package ru.cherryngine.lib.minecraft.codec

import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import java.util.function.Function

data class TypedMapCodec<K, V>(
    val keyCodec: Codec<K>,
    val valueMapper: Function<K, Codec<V>>,
    val maxSize: Int,
    val mutableResolvedValueCache: MutableMap<K, Codec<V>>? = null
) : Codec<Map<K, V>> {
    override fun <D> decode(transcoder: Transcoder<D>, value: D): Map<K, V> {
        val map = transcoder.decodeMap(value)

        if (map.size > maxSize)
            throw IllegalArgumentException("Map size exceeds maximum allowed size: $maxSize")

        if (map.isEmpty) return emptyMap()

        val decoded = HashMap<K, V>(map.size)

        for (keyStr in map.getKeys()) {
            val decodedKey = keyCodec.decode(transcoder, transcoder.encodeString(keyStr))

            val valueCodec = mutableResolvedValueCache?.computeIfAbsent(decodedKey) { valueMapper.apply(it) }
                ?: valueMapper.apply(decodedKey)

            val rawValue = map.getValue(keyStr)

            val valueResult = valueCodec.decode(transcoder, rawValue)

            decoded[decodedKey] = valueResult
        }

        return decoded
    }

    override fun <D> encode(transcoder: Transcoder<D>, value: Map<K, V>): D {

        if (value.size > maxSize)
            throw IllegalArgumentException("Map size exceeds maximum allowed size: $maxSize")

        if (value.isEmpty()) return transcoder.emptyMap()

        val builder = transcoder.encodeMap()

        for ((k, v) in value) {
            val encodedKey = keyCodec.encode(transcoder, k)

            val valueCodec = mutableResolvedValueCache?.computeIfAbsent(k) { valueMapper.apply(k) }
                ?: valueMapper.apply(k)

            val valueResult = valueCodec.encode(transcoder, v)

            builder.put(encodedKey, valueResult)
        }

        return builder.build()
    }
}
