package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.text.Component

data class ClientboundLoginDisconnectPacket(
    val reason: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentCodecs.JSON, ClientboundLoginDisconnectPacket::reason,
            ::ClientboundLoginDisconnectPacket
        )
    }
}