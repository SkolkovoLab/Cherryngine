package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ClientboundResourcePackPopPacket(
    val uuid: UUID?,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID.optional(), ClientboundResourcePackPopPacket::uuid,
            ::ClientboundResourcePackPopPacket
        )
    }
}