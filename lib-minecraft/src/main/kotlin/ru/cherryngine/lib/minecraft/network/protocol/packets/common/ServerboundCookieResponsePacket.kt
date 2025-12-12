package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundCookieResponsePacket(
    val identifier: Key,
    val payload: ByteArray?,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ServerboundCookieResponsePacket::identifier,
            StreamCodec.BYTE_ARRAY.optional(), ServerboundCookieResponsePacket::payload,
            ::ServerboundCookieResponsePacket
        )
    }
}