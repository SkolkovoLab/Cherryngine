package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundDisconnectPacket(
    val reason: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.NBT, ClientboundDisconnectPacket::reason,
            ::ClientboundDisconnectPacket
        )
    }
}