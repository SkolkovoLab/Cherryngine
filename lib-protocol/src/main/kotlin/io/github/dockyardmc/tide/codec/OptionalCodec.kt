package io.github.dockyardmc.tide.codec

import io.github.dockyardmc.tide.transcoder.Transcoder

class OptionalCodec<T>(
    val inner: Codec<T>
) : Codec<T?> {

    override fun <D> encode(transcoder: Transcoder<D>, value: T?): D {
        if (value == null) return transcoder.encodeNull()
        return inner.encode(transcoder, value)
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): T? {
        val result = runCatching {
            inner.decode(transcoder, value)
        }
        if (result.isSuccess) return result.getOrThrow()
        return null
    }
}