package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundBlockDestructionPacket(
    val breakerId: Int,
    val location: Vec3D,
    val destroyStage: Byte
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundBlockDestructionPacket::breakerId,
            LocationCodecs.VEC_3D, ClientboundBlockDestructionPacket::location,
            StreamCodec.BYTE, ClientboundBlockDestructionPacket::destroyStage,
            ::ClientboundBlockDestructionPacket
        )
    }
}