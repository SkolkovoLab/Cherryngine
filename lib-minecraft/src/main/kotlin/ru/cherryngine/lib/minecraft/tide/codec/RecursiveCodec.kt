package ru.cherryngine.lib.minecraft.tide.codec

import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder

data class RecursiveCodec<T>(
    val self: (Codec<T>) -> Codec<T>
) : Codec<T> {

    val delegate = self.invoke(this)

    override fun <D> encode(transcoder: Transcoder<D>, value: T): D {
        return delegate.encode(transcoder, value)
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): T {
        return delegate.decode(transcoder, value)
    }

}