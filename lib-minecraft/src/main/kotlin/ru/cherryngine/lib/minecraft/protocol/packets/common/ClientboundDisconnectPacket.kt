package ru.cherryngine.lib.minecraft.protocol.packets.common

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundDisconnectPacket(
    val reason: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.Companion.of(
            ComponentCodecs.NBT, ClientboundDisconnectPacket::reason,
            ::ClientboundDisconnectPacket
        )
    }
}