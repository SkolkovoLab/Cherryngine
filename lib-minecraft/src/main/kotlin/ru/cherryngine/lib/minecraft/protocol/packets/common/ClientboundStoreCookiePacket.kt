package ru.cherryngine.lib.minecraft.protocol.packets.common

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundStoreCookiePacket(
    val identifier: Key,
    val payload: ByteArray,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ClientboundStoreCookiePacket::identifier,
            StreamCodec.BYTE_ARRAY, ClientboundStoreCookiePacket::payload,
            ::ClientboundStoreCookiePacket
        )
    }
}