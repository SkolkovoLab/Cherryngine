package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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