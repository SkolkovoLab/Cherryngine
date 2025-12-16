package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.TagRegistry

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