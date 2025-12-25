package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.BinaryTagCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class BucketEntityDataComponent(
    val nbt: CompoundBinaryTag
) : DataComponent() {

    companion object {
        val CODEC = BinaryTagCodec.COMPOUND_CODEC.transform(
            ::BucketEntityDataComponent,
            BucketEntityDataComponent::nbt
        )
        val STREAM_CODEC = StreamCodec.of(
            BinaryTagStreamCodecs.COMPOUND_STREAM, BucketEntityDataComponent::nbt,
            ::BucketEntityDataComponent
        )
    }
}