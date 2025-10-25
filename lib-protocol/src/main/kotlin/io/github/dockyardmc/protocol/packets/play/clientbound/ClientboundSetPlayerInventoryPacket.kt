package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

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