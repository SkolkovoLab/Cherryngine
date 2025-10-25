package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class BucketEntityDataComponent(
    val nbt: CompoundBinaryTag
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(BucketEntityDataComponent::class)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodecNBT.COMPOUND_STREAM, BucketEntityDataComponent::nbt,
            ::BucketEntityDataComponent
        )
    }
}