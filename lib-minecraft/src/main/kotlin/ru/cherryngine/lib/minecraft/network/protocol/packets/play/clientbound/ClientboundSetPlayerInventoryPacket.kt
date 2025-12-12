package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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