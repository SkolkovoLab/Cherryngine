package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.HashList
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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