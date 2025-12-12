package ru.cherryngine.lib.minecraft.network.protocol.packets.login

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundLoginDisconnectPacket(
    val reason: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.JSON, ClientboundLoginDisconnectPacket::reason,
            ::ClientboundLoginDisconnectPacket
        )
    }
}