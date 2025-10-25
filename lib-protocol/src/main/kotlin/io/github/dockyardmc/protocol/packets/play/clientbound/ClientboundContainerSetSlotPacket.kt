package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ClientboundContainerSetSlotPacket(
    val windowId: Int,
    val stateId: Int,
    val slot: Int,
    val itemStack: ItemStack
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundContainerSetSlotPacket::windowId,
            StreamCodec.VAR_INT, ClientboundContainerSetSlotPacket::stateId,
            StreamCodec.INT_SHORT, ClientboundContainerSetSlotPacket::slot,
            ItemStack.STREAM_CODEC, ClientboundContainerSetSlotPacket::itemStack,
            ::ClientboundContainerSetSlotPacket
        )
    }
}