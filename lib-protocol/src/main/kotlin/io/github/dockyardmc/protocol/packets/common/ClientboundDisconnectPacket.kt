package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.text.Component

data class ClientboundDisconnectPacket(
    val reason: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.Companion.of(
            ComponentCodecs.NBT, ClientboundDisconnectPacket::reason,
            ::ClientboundDisconnectPacket
        )
    }
}