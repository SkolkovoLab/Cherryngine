package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer

class ItemNameComponent(
    val itemName: Component
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        val nbt: BinaryTag = NBTComponentSerializer.nbt().serialize(itemName)
        return StaticHash(CRC32CHasher.ofNbt(nbt))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentCodecs.NBT, ItemNameComponent::itemName,
            ::ItemNameComponent
        )
    }
}