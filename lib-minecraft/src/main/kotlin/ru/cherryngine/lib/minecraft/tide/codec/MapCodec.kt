package ru.cherryngine.lib.minecraft.tide.codec

import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder

class MapCodec<K, V>(
    val keyCodec: Codec<K>,
    val valueCodec: Codec<V>
) : Codec<Map<K, V>> {

    override fun <D> encode(transcoder: Transcoder<D>, value: Map<K, V>): D {
        val mapBuilder = transcoder.encodeMap()
        value.forEach { entry ->
            val keyResult = keyCodec.encode(transcoder, entry.key)
            val valueResult = valueCodec.encode(transcoder, entry.value)

            mapBuilder.put(keyResult, valueResult)
        }

        return mapBuilder.build()
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): Map<K, V> {
        val mapResult = transcoder.decodeMap(value)
        val decodedMap = mutableMapOf<K, V>()

        mapResult.getKeys().forEach { key ->
            val keyResult = keyCodec.decode(transcoder, transcoder.encodeString(key))
            val valueResult = valueCodec.decode(transcoder, mapResult.getValue(key))
            decodedMap[keyResult] = valueResult
        }

        return decodedMap
    }
}