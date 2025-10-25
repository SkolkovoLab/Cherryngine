package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

class ClientboundSetCursorItemPacket(
    val itemStack: ItemStack
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC, ClientboundSetCursorItemPacket::itemStack,
            ::ClientboundSetCursorItemPacket
        )
    }
}