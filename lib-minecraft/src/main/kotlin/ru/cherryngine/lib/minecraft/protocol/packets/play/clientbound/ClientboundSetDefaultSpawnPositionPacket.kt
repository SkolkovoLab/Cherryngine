package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSetDefaultSpawnPositionPacket(
    val location: Vec3I,
    val angle: Float,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.BLOCK_POSITION, ClientboundSetDefaultSpawnPositionPacket::location,
            StreamCodec.FLOAT, ClientboundSetDefaultSpawnPositionPacket::angle,
            ::ClientboundSetDefaultSpawnPositionPacket
        )
    }
}