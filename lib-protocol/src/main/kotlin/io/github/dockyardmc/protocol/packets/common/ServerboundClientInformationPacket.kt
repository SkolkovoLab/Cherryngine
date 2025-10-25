package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.types.ClientSettings
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundClientInformationPacket(
    val clientSettings: ClientSettings
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ClientSettings.STREAM_CODEC, ServerboundClientInformationPacket::clientSettings,
            ::ServerboundClientInformationPacket
        )
    }
}