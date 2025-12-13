package ru.cherryngine.lib.minecraft.entity

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.protocol.types.ClientSettings
import ru.cherryngine.lib.minecraft.network.protocol.types.Direction
import ru.cherryngine.lib.minecraft.network.protocol.types.ResolvableProfile
import ru.cherryngine.lib.minecraft.network.protocol.types.VillagerData
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.Registries
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
            val COMPONENT = Type(index++, ComponentStreamCodecs.NBT).also { entries.add(it) }
            val OPT_COMPONENT = Type(index++, ComponentStreamCodecs.NBT.optional()).also { entries.add(it) }
            val ITEM_STACK = Type(index++, ItemStack.STREAM_CODEC).also { entries.add(it) }
            val BOOLEAN = Type(index++, StreamCodec.BOOLEAN).also { entries.add(it) }
            val ROTATION = Type(index++, LocationStreamCodecs.VEC_3D).also { entries.add(it) }
            val BLOCK_POSITION = Type(index++, LocationStreamCodecs.BLOCK_POSITION).also { entries.add(it) }
            val OPT_BLOCK_POSITION = Type(index++, LocationStreamCodecs.BLOCK_POSITION.optional()).also { entries.add(it) }
            val DIRECTION = Type(index++, EnumStreamCodec<Direction>()).also { entries.add(it) }
            val OPT_UUID = Type(index++, StreamCodec.UUID.optional()).also { entries.add(it) }
            val BLOCK_STATE = Type(index++, Block.STREAM_CODEC).also { entries.add(it) }
            val OPT_BLOCK_STATE = Type(index++, Block.STREAM_CODEC).also { entries.add(it) }
            val PARTICLE = Type(index++, Registries.particle.streamCodec).also { entries.add(it) }
            val PARTICLE_LIST = Type(index++, Registries.particle.streamCodec.list()).also { entries.add(it) }
            val VILLAGER_DATA = Type(index++, VillagerData.STREAM_CODEC).also { entries.add(it) }
            val OPT_VAR_INT = Type(index++, StreamCodec.OPT_VAR_INT).also { entries.add(it) }
            val ENTITY_POSE = Type(index++, EnumStreamCodec<EntityMeta.Pose>()).also { entries.add(it) }
            val CAT_VARIANT = Type(index++, Registries.catVariant.streamCodec).also { entries.add(it) }
            val COW_VARIANT = Type(index++, Registries.cowVariant.streamCodec).also { entries.add(it) }
            val WOLF_VARIANT = Type(index++, Registries.wolfVariant.streamCodec).also { entries.add(it) }
            val WOLF_SOUND_VARIANT = Type(index++, Registries.wolfSoundVariant.streamCodec).also { entries.add(it) }
            val FROG_VARIANT = Type(index++, Registries.frogVariant.streamCodec).also { entries.add(it) }
            val PIG_VARIANT = Type(index++, Registries.pigVariant.streamCodec).also { entries.add(it) }
            val CHICKEN_VARIANT = Type(index++, Registries.chickenVariant.streamCodec).also { entries.add(it) }
            val ZOMBIE_NAUTILUS_VARIANT = Type(index++, Registries.zombieNautilusVariant.streamCodec).also { entries.add(it) }
            val OPT_GLOBAL_POSITION = Type(index++, StreamCodec.UNIT).also { entries.add(it) } // Unused by protocol it seems
            val PAINTING_VARIANT = Type(index++, Registries.paintingVariant.streamCodec).also { entries.add(it) }
            val SNIFFER_STATE = Type(index++, EnumStreamCodec<SnifferMeta.State>()).also { entries.add(it) }
            val ARMADILLO_STATE = Type(index++, EnumStreamCodec<ArmadilloMeta.State>()).also { entries.add(it) }
            val COPPER_GOLEM_STATE = Type(index++, EnumStreamCodec<CopperGolemMeta.State>()).also { entries.add(it) }
            val WEATHER_STATE = Type(index++, EnumStreamCodec<CopperGolemMeta.WeatherState>()).also { entries.add(it) }
            val VECTOR3 = Type(index++, LocationStreamCodecs.VEC_3D_FLOAT).also { entries.add(it) }
            val QUATERNION = Type(index++, LocationStreamCodecs.QUATERNION).also { entries.add(it) }
            val RESOLVABLE_PROFILE = Type(index++, ResolvableProfile.STREAM_CODEC).also { entries.add(it) }
            val MAIN_HAND = Type(index++, EnumStreamCodec<ClientSettings.MainHand>()).also { entries.add(it) }

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