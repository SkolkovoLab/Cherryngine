package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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