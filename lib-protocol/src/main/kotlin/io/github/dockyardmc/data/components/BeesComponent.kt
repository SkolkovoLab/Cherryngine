package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.HashList
import io.github.dockyardmc.protocol.DataComponentHashable
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.CompoundBinaryTag

class BeesComponent(val bees: List<Bee>) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return HashList(bees.map { bee -> bee.hashStruct() })
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Bee.STREAM_CODEC.list(), BeesComponent::bees,
            ::BeesComponent
        )
    }

    data class Bee(
        val entityData: CompoundBinaryTag,
        val ticksInHive: Int,
        val minTicksInHive: Int
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("entity_data", CRC32CHasher.ofNbt(entityData))
                static("ticks_in_hive", CRC32CHasher.ofInt(ticksInHive))
                static("min_ticks_in_hive", CRC32CHasher.ofInt(minTicksInHive))
            }
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodecNBT.COMPOUND_STREAM, Bee::entityData,
                StreamCodec.VAR_INT, Bee::ticksInHive,
                StreamCodec.VAR_INT, Bee::minTicksInHive,
                ::Bee
            )
        }
    }
}