package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundBlockDestructionPacket(
    val breakerId: Int,
    val location: Vec3D,
    val destroyStage: Byte
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundBlockDestructionPacket::breakerId,
            LocationStreamCodecs.VEC_3D, ClientboundBlockDestructionPacket::location,
            StreamCodec.BYTE, ClientboundBlockDestructionPacket::destroyStage,
            ::ClientboundBlockDestructionPacket
        )
    }
}