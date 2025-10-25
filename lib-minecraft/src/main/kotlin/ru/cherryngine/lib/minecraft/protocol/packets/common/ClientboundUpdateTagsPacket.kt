package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.tags.TagRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundUpdateTagsPacket(
    val registries: List<TagRegistry>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            TagRegistry.STREAM_CODEC.list(), ClientboundUpdateTagsPacket::registries,
            ::ClientboundUpdateTagsPacket
        )
    }
}