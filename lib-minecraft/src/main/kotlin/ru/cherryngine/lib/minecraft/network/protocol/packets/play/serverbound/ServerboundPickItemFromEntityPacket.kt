package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundPickItemFromEntityPacket(
    val entityId: Int,
    val includeData: Boolean
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundPickItemFromEntityPacket::entityId,
            StreamCodec.BOOLEAN, ServerboundPickItemFromEntityPacket::includeData,
            ::ServerboundPickItemFromEntityPacket
        )
    }
}