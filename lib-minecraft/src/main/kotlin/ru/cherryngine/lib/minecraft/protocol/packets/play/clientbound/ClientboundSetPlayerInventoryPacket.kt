package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ClientboundSetPlayerInventoryPacket(
    val slot: Int,
    val itemStack: ItemStack
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetPlayerInventoryPacket::slot,
            ItemStack.STREAM_CODEC, ClientboundSetPlayerInventoryPacket::itemStack,
            ::ClientboundSetPlayerInventoryPacket
        )
    }
}