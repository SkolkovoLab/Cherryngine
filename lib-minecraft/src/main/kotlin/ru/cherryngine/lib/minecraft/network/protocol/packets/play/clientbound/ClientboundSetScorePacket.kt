package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetScorePacket(
    val entityName: String,
    val objectiveName: String,
    val value: Int,
    val displayName: Component?,
    val numberFormat: Int?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundSetScorePacket::entityName,
            StreamCodec.STRING, ClientboundSetScorePacket::objectiveName,
            StreamCodec.VAR_INT, ClientboundSetScorePacket::value,
            ComponentStreamCodecs.NBT.optional(), ClientboundSetScorePacket::displayName,
            StreamCodec.VAR_INT.optional(), ClientboundSetScorePacket::numberFormat,
            ::ClientboundSetScorePacket
        )
    }
}