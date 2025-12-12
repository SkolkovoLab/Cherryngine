package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.EntityAnimation
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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
