package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.EntityAnimation
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundAnimatePacket(
    val entityId: Int,
    val animation: EntityAnimation
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundAnimatePacket::entityId,
            EnumStreamCodec<EntityAnimation>(), ClientboundAnimatePacket::animation,
            ::ClientboundAnimatePacket
        )
    }
}
