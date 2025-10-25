package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.CompoundBinaryTag

class CustomDataComponent(
    val nbt: CompoundBinaryTag
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofNbt(nbt))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodecNBT.COMPOUND_STREAM, CustomDataComponent::nbt,
            ::CustomDataComponent
        )
    }
}