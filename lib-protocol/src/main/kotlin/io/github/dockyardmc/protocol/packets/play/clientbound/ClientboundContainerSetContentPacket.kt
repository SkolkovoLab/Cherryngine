package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ClientboundContainerSetContentPacket(
    val windowId: Int,
    val stateId: Int,
    val items: List<ItemStack>,
    val carriedItem: ItemStack,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundContainerSetContentPacket::windowId,
            StreamCodec.VAR_INT, ClientboundContainerSetContentPacket::stateId,
            ItemStack.STREAM_CODEC.list(), ClientboundContainerSetContentPacket::items,
            ItemStack.STREAM_CODEC, ClientboundContainerSetContentPacket::carriedItem,
            ::ClientboundContainerSetContentPacket
        )
    }
}