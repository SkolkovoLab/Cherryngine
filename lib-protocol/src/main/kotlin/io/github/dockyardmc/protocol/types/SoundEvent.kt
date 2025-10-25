package io.github.dockyardmc.protocol.types

import io.github.dockyardmc.codec.IdOrXStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.protocol.DataComponentHashable
import io.github.dockyardmc.registry.registries.SoundRegistry
import io.github.dockyardmc.tide.codec.Codec
import io.github.dockyardmc.tide.codec.StructCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.tide.transcoder.Transcoder
import io.github.dockyardmc.tide.types.Either
import net.kyori.adventure.key.Key

sealed interface SoundEvent : DataComponentHashable {
    val identifier: Key

    companion object {
        val CODEC = object : Codec<SoundEvent> {
            override fun <D> encode(transcoder: Transcoder<D>, value: SoundEvent): D {
                return when (value) {
                    is BuiltinSoundEvent -> transcoder.encodeString(value.identifier.asString())
                    is CustomSoundEvent -> CustomSoundEvent.CODEC.encode(transcoder, value)
                }
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): SoundEvent {
                val result = runCatching { transcoder.decodeString(value) }
                if (result.isSuccess) return BuiltinSoundEvent(Key.key(result.getOrThrow()))

                return CustomSoundEvent.CODEC.decode(transcoder, value)
            }
        }

        val STREAM_CODEC: StreamCodec<SoundEvent> = IdOrXStreamCodec(CustomSoundEvent.STREAM_CODEC)
            .transform<SoundEvent>({
                when (it) {
                    is Either.Left -> BuiltinSoundEvent(it.value)
                    is Either.Right -> it.value
                }
            }, {
                when (it) {
                    is BuiltinSoundEvent -> Either.Left(it.protocolId)
                    is CustomSoundEvent -> Either.Right(it)
                }
            })
    }

    data class BuiltinSoundEvent(
        override val identifier: Key,
    ) : SoundEvent {
        constructor(id: Int) : this(Key.key(SoundRegistry.getByProtocolId(id)))

        val protocolId = SoundRegistry[identifier.asString()]

        override fun hashStruct(): HashHolder {
            return StaticHash(CRC32CHasher.ofString(identifier.asString()))
        }
    }

    data class CustomSoundEvent(
        override val identifier: Key,
        val range: Float? = null,
    ) : SoundEvent {
        companion object {
            val CODEC = StructCodec.of(
                "sound_id", Codec.KEY, CustomSoundEvent::identifier,
                "range", Codec.FLOAT.optional(), CustomSoundEvent::range,
                ::CustomSoundEvent
            )

            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.KEY, CustomSoundEvent::identifier,
                StreamCodec.FLOAT.optional(), CustomSoundEvent::range,
                ::CustomSoundEvent
            )
        }


        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("sound_id", CRC32CHasher.ofString(identifier.asString()))
                optional("range", range, CRC32CHasher::ofFloat)
            }
        }
    }
}
