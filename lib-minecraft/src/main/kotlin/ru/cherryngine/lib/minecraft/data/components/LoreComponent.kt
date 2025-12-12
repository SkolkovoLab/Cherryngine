package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class LoreComponent(
    val lore: List<Component>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofList(lore.map { component ->
            val nbt: BinaryTag = NBTComponentSerializer.nbt().serialize(component)
            CRC32CHasher.ofNbt(nbt)
        }))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.NBT.list(), LoreComponent::lore,
            ::LoreComponent
        )
    }
}