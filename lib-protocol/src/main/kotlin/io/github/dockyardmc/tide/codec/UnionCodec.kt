package io.github.dockyardmc.tide.codec

import io.github.dockyardmc.tide.transcoder.Transcoder

data class UnionCodec<T, R>(
    val keyFiled: String,
    val keyCodec: Codec<T>,
    val serializers: (T) -> StructCodec<out R>,
    val keyFunc: (R) -> T
) : StructCodec<R> {

    override fun <T> decodeFromMap(transcoder: Transcoder<T>, map: Transcoder.VirtualMap<T>): R {
        val key = keyCodec.decode(transcoder, map.getValue(keyFiled))
        val serializer = serializers.invoke(key)
        return serializer.decodeFromMap(transcoder, map)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> encodeToMap(transcoder: Transcoder<T>, value: R, map: Transcoder.VirtualMapBuilder<T>): T {
        val key = keyFunc.invoke(value)
        val serializer = serializers.invoke(key) as StructCodec<R>
        map.put(keyFiled, keyCodec.encode(transcoder, key))
        return serializer.encodeToMap(transcoder, value, map)
    }
}