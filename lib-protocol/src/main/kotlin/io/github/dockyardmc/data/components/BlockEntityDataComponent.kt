package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.CompoundBinaryTag

class BlockEntityDataComponent(
    val nbt: CompoundBinaryTag
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodecNBT.COMPOUND_STREAM, BlockEntityDataComponent::nbt,
            ::BlockEntityDataComponent
        )
    }
}