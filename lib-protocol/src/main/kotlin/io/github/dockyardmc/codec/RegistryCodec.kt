package io.github.dockyardmc.codec

import io.github.dockyardmc.registry.Registry
import io.github.dockyardmc.registry.RegistryEntry
import io.github.dockyardmc.tide.codec.Codec
import io.github.dockyardmc.tide.transcoder.Transcoder

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

