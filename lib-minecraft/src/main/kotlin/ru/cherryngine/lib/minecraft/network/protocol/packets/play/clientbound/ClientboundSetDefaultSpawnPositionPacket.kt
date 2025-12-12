package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetDefaultSpawnPositionPacket(
    val location: Vec3I,
    val angle: Float,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationStreamCodecs.BLOCK_POSITION, ClientboundSetDefaultSpawnPositionPacket::location,
            StreamCodec.FLOAT, ClientboundSetDefaultSpawnPositionPacket::angle,
            ::ClientboundSetDefaultSpawnPositionPacket
        )
    }
}