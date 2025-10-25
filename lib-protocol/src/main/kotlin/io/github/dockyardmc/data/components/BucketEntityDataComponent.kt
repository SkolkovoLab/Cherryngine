package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.CompoundBinaryTag

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