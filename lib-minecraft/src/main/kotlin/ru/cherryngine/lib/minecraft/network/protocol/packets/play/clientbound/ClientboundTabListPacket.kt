package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundTabListPacket(
    val header: Component,
    val footer: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.NBT, ClientboundTabListPacket::header,
            ComponentStreamCodecs.NBT, ClientboundTabListPacket::footer,
            ::ClientboundTabListPacket
        )
    }
}