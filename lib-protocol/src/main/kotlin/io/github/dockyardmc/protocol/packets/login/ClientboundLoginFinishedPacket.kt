package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.GameProfile
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundLoginFinishedPacket(
    val gameProfile: GameProfile
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            GameProfile.STREAM_CODEC, ClientboundLoginFinishedPacket::gameProfile,
            ::ClientboundLoginFinishedPacket
        )
    }
}