package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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