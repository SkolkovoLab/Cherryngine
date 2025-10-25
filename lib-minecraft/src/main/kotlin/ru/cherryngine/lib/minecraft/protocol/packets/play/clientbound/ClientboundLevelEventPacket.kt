package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.LevelEvent
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundLevelEventPacket(
    val event: LevelEvent,
    val location: Vec3I,
    val extraData: Int,
    val disableRelativeVolume: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LevelEvent.STREAM_CODEC, ClientboundLevelEventPacket::event,
            LocationCodecs.BLOCK_POSITION, ClientboundLevelEventPacket::location,
            StreamCodec.INT, ClientboundLevelEventPacket::extraData,
            StreamCodec.BOOLEAN, ClientboundLevelEventPacket::disableRelativeVolume,
            ::ClientboundLevelEventPacket
        )
    }
}