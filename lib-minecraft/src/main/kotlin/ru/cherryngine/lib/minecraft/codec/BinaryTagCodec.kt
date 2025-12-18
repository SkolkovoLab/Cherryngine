package ru.cherryngine.lib.minecraft.codec

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.data.NbtHasher

object BinaryTagCodec {
    val CODEC = object : Codec<BinaryTag> {
        override fun <D> encode(
            transcoder: Transcoder<D>,
            value: BinaryTag,
        ): D {
            when (transcoder) {
                is BinaryTagTranscoder -> {
                    @Suppress("UNCHECKED_CAST")
                    return value as D
                }

                is CRC32CTranscoder -> {
                    @Suppress("UNCHECKED_CAST")
                    return NbtHasher.hashTag(value) as D
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }
        }

        override fun <D> decode(
            transcoder: Transcoder<D>,
            value: D,
        ): BinaryTag {
            return when (transcoder) {
                is BinaryTagTranscoder -> {
                    value as BinaryTag
                }

                is CRC32CTranscoder -> {
                    throw UnsupportedOperationException()
                }

                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }

    val COMPOUND_CODEC: Codec<CompoundBinaryTag> = CODEC.transform({ it as CompoundBinaryTag }, { it })
}