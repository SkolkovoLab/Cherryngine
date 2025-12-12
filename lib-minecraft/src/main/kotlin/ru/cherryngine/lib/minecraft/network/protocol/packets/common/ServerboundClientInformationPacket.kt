package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ClientSettings
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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