package ru.cherryngine.lib.minecraft.tide.codec

import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder

class ListCodec<T>(
    val inner: Codec<T>
) : Codec<List<T>> {

    override fun <D> encode(transcoder: Transcoder<D>, value: List<T>): D {
        val encodedList = transcoder.encodeList(value.size)
        value.forEach { item ->
            encodedList.add(inner.encode(transcoder, item))
        }
        return encodedList.build()
    }

    override fun <D> decode(transcoder: Transcoder<D>, value: D): List<T> {
        val listResult = transcoder.decodeList(value)
        val decodedList = mutableListOf<T>()
        listResult.forEach { item ->
            decodedList.add(inner.decode(transcoder, item))
        }
        return decodedList
    }
}