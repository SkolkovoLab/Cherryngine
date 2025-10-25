package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.CompoundBinaryTag

class LockComponent(
    val data: CompoundBinaryTag
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodecNBT.COMPOUND_STREAM, LockComponent::data,
            ::LockComponent
        )
    }
}