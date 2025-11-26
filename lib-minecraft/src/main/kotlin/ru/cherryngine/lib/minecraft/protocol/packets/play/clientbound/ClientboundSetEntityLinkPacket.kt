package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetEntityLinkPacket(
    val attachedEntityId: Int,
    val holdingEntityId: Int, // Set to -1 to detach
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT, ClientboundSetEntityLinkPacket::attachedEntityId,
            StreamCodec.INT, ClientboundSetEntityLinkPacket::holdingEntityId,
            ::ClientboundSetEntityLinkPacket
        )
    }
}