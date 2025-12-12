package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import java.util.*

class ServerboundTeleportToEntityPacket(
    val target: UUID
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID, ServerboundTeleportToEntityPacket::target,
            ::ServerboundTeleportToEntityPacket
        )
    }
}