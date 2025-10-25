package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSystemChatPacket(
    val component: Component,
    val isActionBar: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentCodecs.NBT, ClientboundSystemChatPacket::component,
            StreamCodec.BOOLEAN, ClientboundSystemChatPacket::isActionBar,
            ::ClientboundSystemChatPacket
        )
    }
}