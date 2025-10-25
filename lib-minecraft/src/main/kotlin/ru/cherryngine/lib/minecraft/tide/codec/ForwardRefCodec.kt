package ru.cherryngine.lib.minecraft.tide.codec

import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder

class ForwardRefCodec<T>(
    delegateFunc: () -> Codec<T>
) : Codec<T> {
    private val delegate = delegateFunc.invoke()

    override fun <D> encode(transcoder: Transcoder<D>, value: T): D {
        return delegate.encode(transcoder, value)
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): T {
        return delegate.decode(transcoder, value)
    }

}