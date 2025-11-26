package ru.cherryngine.lib.minecraft.protocol.packets.common

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundCookieRequestPacket(
    val identifier: Key,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ClientboundCookieRequestPacket::identifier,
            ::ClientboundCookieRequestPacket
        )
    }
}