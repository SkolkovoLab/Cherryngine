package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundPickItemFromBlockPacket(
    val blockPosition: Vec3I,
    val includeData: Boolean
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.BLOCK_POSITION, ServerboundPickItemFromBlockPacket::blockPosition,
            StreamCodec.BOOLEAN, ServerboundPickItemFromBlockPacket::includeData,
            ::ServerboundPickItemFromBlockPacket
        )
    }
}