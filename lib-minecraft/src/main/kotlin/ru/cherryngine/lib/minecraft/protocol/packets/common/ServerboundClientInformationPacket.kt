package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ClientSettings
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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