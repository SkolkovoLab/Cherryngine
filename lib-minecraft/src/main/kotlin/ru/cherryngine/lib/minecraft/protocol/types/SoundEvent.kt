package ru.cherryngine.lib.minecraft.protocol.types

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.IdOrXStreamCodec
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.registry.registries.SoundRegistry
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.tide.types.Either

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
