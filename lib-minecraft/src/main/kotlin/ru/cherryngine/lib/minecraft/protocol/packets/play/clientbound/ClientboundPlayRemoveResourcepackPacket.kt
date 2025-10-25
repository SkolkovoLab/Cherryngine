package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ClientboundPlayRemoveResourcepackPacket(
    val uuid: UUID?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID.optional(), ClientboundPlayRemoveResourcepackPacket::uuid,
            ::ClientboundPlayRemoveResourcepackPacket
        )
    }
}