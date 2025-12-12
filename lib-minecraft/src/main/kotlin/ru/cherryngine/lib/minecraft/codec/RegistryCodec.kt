package ru.cherryngine.lib.minecraft.codec

import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

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

