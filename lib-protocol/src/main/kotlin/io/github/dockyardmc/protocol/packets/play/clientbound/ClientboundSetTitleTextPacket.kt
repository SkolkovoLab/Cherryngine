package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.text.Component

data class ClientboundSetTitleTextPacket(
    val component: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentCodecs.NBT, ClientboundSetTitleTextPacket::component,
            ::ClientboundSetTitleTextPacket
        )
    }
}