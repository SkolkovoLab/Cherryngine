package ru.cherryngine.lib.minecraft.codec

import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder

object RegistryCodec {
    fun <T : RegistryEntry> codec(registry: Registry<T>): Codec<T> {
        return object : Codec<T> {
            override fun <D> encode(transcoder: Transcoder<D>, value: T): D {
                return transcoder.encodeString(value.getEntryIdentifier())
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): T {
                return registry[transcoder.decodeString(value)]
            }
        }
    }
}

