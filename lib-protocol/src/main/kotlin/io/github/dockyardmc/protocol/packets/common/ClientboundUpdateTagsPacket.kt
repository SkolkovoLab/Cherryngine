package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.registries.tags.TagRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

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