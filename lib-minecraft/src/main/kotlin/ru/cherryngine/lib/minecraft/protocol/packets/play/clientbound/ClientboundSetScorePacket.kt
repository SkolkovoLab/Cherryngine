package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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
            ComponentCodecs.NBT.optional(), ClientboundSetScorePacket::displayName,
            StreamCodec.VAR_INT.optional(), ClientboundSetScorePacket::numberFormat,
            ::ClientboundSetScorePacket
        )
    }
}