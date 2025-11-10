package ru.cherryngine.lib.minecraft.entity

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.types.Direction
import ru.cherryngine.lib.minecraft.protocol.types.VillagerData
import ru.cherryngine.lib.minecraft.registry.registries.*
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block

@Suppress("UNCHECKED_CAST")
class MetadataEntry<T>(
    val type: Type<T>,
    val value: T,
) {
    class Type<T>(
        val index: Int,
        val streamCodec: StreamCodec<T>,
    ) {
        fun entry(value: T) = MetadataEntry(this, value)

        companion object {
            private val entries = mutableListOf<Type<*>>()
            private var index = 0

            val BYTE = Type(index++, StreamCodec.BYTE).also { entries.add(it) }
            val VAR_INT = Type(index++, StreamCodec.VAR_INT).also { entries.add(it) }
            val VAR_LONG = Type(index++, StreamCodec.VAR_LONG).also { entries.add(it) }
            val FLOAT = Type(index++, StreamCodec.FLOAT).also { entries.add(it) }
            val STRING = Type(index++, StreamCodec.STRING).also { entries.add(it) }
            val COMPONENT = Type(index++, ComponentCodecs.NBT).also { entries.add(it) }
            val OPT_COMPONENT = Type(index++, ComponentCodecs.NBT.optional()).also { entries.add(it) }
            val ITEM_STACK = Type(index++, ItemStack.STREAM_CODEC).also { entries.add(it) }
            val BOOLEAN = Type(index++, StreamCodec.BOOLEAN).also { entries.add(it) }
            val ROTATION = Type(index++, LocationCodecs.VEC_3D).also { entries.add(it) }
            val BLOCK_POSITION = Type(index++, LocationCodecs.BLOCK_POSITION).also { entries.add(it) }
            val OPT_BLOCK_POSITION = Type(index++, LocationCodecs.BLOCK_POSITION.optional()).also { entries.add(it) }
            val DIRECTION = Type(index++, EnumStreamCodec<Direction>()).also { entries.add(it) }
            val OPT_UUID = Type(index++, StreamCodec.UUID.optional()).also { entries.add(it) }
            val BLOCK_STATE = Type(index++, Block.STREAM_CODEC).also { entries.add(it) }
            val OPT_BLOCK_STATE = Type(index++, Block.STREAM_CODEC).also { entries.add(it) }
            val NBT = Type(index++, StreamCodecNBT.STREAM).also { entries.add(it) }
            val PARTICLE = Type(index++, ParticleRegistry.STREAM_CODEC).also { entries.add(it) }
            val PARTICLE_LIST = Type(index++, ParticleRegistry.STREAM_CODEC.list()).also { entries.add(it) }
            val VILLAGER_DATA = Type(index++, VillagerData.STREAM_CODEC).also { entries.add(it) }
            val OPT_VAR_INT = Type(index++, StreamCodec.OPT_VAR_INT).also { entries.add(it) }
            val ENTITY_POSE = Type(index++, EnumStreamCodec<EntityMeta.Pose>()).also { entries.add(it) }
            val CAT_VARIANT = Type(index++, CatVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val COW_VARIANT = Type(index++, CowVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val WOLF_VARIANT = Type(index++, WolfVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val WOLF_SOUND_VARIANT = Type(index++, WolfSoundVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val FROG_VARIANT = Type(index++, FrogVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val PIG_VARIANT = Type(index++, PigVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val CHICKEN_VARIANT = Type(index++, ChickenVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val OPT_GLOBAL_POSITION = Type(index++, StreamCodec.UNIT).also { entries.add(it) } // Unused by protocol it seems
            val PAINTING_VARIANT = Type(index++, PaintingVariantRegistry.STREAM_CODEC).also { entries.add(it) }
            val SNIFFER_STATE = Type(index++, EnumStreamCodec<SnifferMeta.State>()).also { entries.add(it) }
            val ARMADILLO_STATE = Type(index++, EnumStreamCodec<ArmadilloMeta.State>()).also { entries.add(it) }
            val VECTOR3 = Type(index++, LocationCodecs.VEC_3D_FLOAT).also { entries.add(it) }
            val QUATERNION = Type(index++, LocationCodecs.QUATERNION).also { entries.add(it) }

            fun fromIndex(index: Int): Type<*> {
                return entries[index]
            }
        }
    }

    companion object {
        val STREAM_CODEC: StreamCodec<MetadataEntry<*>> = object : StreamCodec<MetadataEntry<*>> {
            override fun write(buffer: ByteBuf, value: MetadataEntry<*>) {
                StreamCodec.VAR_INT.write(buffer, value.type.index)
                (value.type.streamCodec as StreamCodec<Any?>).write(buffer, value.value)
            }

            override fun read(buffer: ByteBuf): MetadataEntry<*> {
                val type = Type.fromIndex(StreamCodec.VAR_INT.read(buffer)) as Type<Any?>
                return MetadataEntry(type, type.streamCodec.read(buffer))
            }
        }
    }
}