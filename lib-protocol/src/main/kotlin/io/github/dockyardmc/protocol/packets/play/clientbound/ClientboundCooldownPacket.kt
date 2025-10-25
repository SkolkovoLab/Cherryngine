package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.key.Key

data class ClientboundCooldownPacket(
    val group: Key,
    val cooldown: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ClientboundCooldownPacket::group,
            StreamCodec.VAR_INT, ClientboundCooldownPacket::cooldown,
            ::ClientboundCooldownPacket
        )
    }
}