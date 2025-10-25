package io.github.dockyardmc.protocol.packets.play.serverbound

import ru.cherryngine.lib.math.Vec3I
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

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