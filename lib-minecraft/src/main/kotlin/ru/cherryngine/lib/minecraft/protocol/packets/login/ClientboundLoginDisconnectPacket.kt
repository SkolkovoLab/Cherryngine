package ru.cherryngine.lib.minecraft.protocol.packets.login

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundLoginDisconnectPacket(
    val reason: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentCodecs.JSON, ClientboundLoginDisconnectPacket::reason,
            ::ClientboundLoginDisconnectPacket
        )
    }
}