package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundPickItemFromBlockPacket(
    val blockPosition: Vec3I,
    val includeData: Boolean
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.BLOCK_POSITION, ServerboundPickItemFromBlockPacket::blockPosition,
            StreamCodec.BOOLEAN, ServerboundPickItemFromBlockPacket::includeData,
            ::ServerboundPickItemFromBlockPacket
        )
    }
}