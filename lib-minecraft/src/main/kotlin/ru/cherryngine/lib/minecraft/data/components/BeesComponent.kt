package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.BinaryTagCodec
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class BeesComponent(
    val bees: List<Bee>
) : DataComponent() {

    companion object {
        val CODEC = Bee.CODEC.list().transform(
            ::BeesComponent,
            BeesComponent::bees
        )
        val STREAM_CODEC = StreamCodec.of(
            Bee.STREAM_CODEC.list(), BeesComponent::bees,
            ::BeesComponent
        )
    }

    data class Bee(
        val entityData: CompoundBinaryTag,
        val ticksInHive: Int,
        val minTicksInHive: Int
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "entity_data", BinaryTagCodec.COMPOUND_CODEC, Bee::entityData,
                "ticks_in_hive", Codec.INT, Bee::ticksInHive,
                "min_ticks_in_hive", Codec.INT, Bee::minTicksInHive,
                ::Bee
            )
            val STREAM_CODEC = StreamCodec.of(
                BinaryTagStreamCodecs.COMPOUND_STREAM, Bee::entityData,
                StreamCodec.VAR_INT, Bee::ticksInHive,
                StreamCodec.VAR_INT, Bee::minTicksInHive,
                ::Bee
            )
        }
    }
}