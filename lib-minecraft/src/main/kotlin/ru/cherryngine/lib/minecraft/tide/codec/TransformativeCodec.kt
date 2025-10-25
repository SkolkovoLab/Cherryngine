package ru.cherryngine.lib.minecraft.tide.codec

import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder

data class TransformativeCodec<T, S>(
    val inner: Codec<T>,
    val to: (T) -> S,
    val from: (S) -> T
) : Codec<S> {

    override fun <D> encode(transcoder: Transcoder<D>, value: S): D {
        return inner.encode(transcoder, from.invoke(value))
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): S {
        val innerValue = inner.decode(transcoder, value)
        return to.invoke(innerValue)
    }

}
