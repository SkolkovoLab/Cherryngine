package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundSelectAdvancementsTabPacket(
    val identifier: Key?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY.optional(), ClientboundSelectAdvancementsTabPacket::identifier,
            ::ClientboundSelectAdvancementsTabPacket
        )
    }
}