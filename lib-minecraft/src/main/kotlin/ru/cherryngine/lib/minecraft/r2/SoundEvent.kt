package ru.cherryngine.lib.minecraft.r2

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.sound.Sound
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.network.stream_codec.IdOrXStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.Either
import ru.cherryngine.lib.minecraft.utils.KeyedKt
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

sealed interface SoundEvent : KeyedKt, Sound.Type, DataComponentHashable {
    override fun key() = key

    companion object {
        val CODEC = object : Codec<SoundEvent> {
            override fun <D> encode(transcoder: Transcoder<D>, value: SoundEvent): D {
                return when (value) {
                    is Builtin -> Codec.KEY.encode(transcoder, value.key)
                    is Custom -> Custom.CODEC.encode(transcoder, value)
                }
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): SoundEvent {
                val result = runCatching { Codec.KEY.decode(transcoder, value) }
                if (result.isSuccess) return Registries.soundEvent[result.getOrThrow()]
                return Custom.CODEC.decode(transcoder, value)
            }
        }

        val STREAM_CODEC: StreamCodec<SoundEvent> = IdOrXStreamCodec(
            Custom.STREAM_CODEC
        ).transform<SoundEvent>({
            when (it) {
                is Either.Left -> Registries.soundEvent[it.value]
                is Either.Right -> it.value
            }
        }, {
            when (it) {
                is Builtin -> Either.Left(it.id)
                is Custom -> Either.Right(it)
            }
        })
    }

    override fun hashStruct(): HashHolder {
        return StaticHash(CODEC.encode(CRC32CTranscoder, this))
    }

    @Serializable
    data class Builtin(
        @Serializable(KeySerializer::class)
        override val key: Key,
        override val id: Int,
    ) : SoundEvent, StaticProtocolObject {
        override fun key() = key
    }

    data class Custom(
        override val key: Key,
        val range: Float?,
    ) : SoundEvent {
        companion object {
            val CODEC = StructCodec.of(
                "sound_id", Codec.KEY, Keyed::key,
                "range", Codec.FLOAT.optional(), Custom::range,
                ::Custom
            )

            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.KEY, Keyed::key,
                StreamCodec.FLOAT.optional(), Custom::range,
                ::Custom
            )
        }
    }
}

