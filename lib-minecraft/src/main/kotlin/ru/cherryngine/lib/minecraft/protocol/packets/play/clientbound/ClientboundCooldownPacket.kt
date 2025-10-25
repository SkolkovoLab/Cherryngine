package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundCooldownPacket(
    val group: Key,
    val cooldown: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ClientboundCooldownPacket::group,
            StreamCodec.VAR_INT, ClientboundCooldownPacket::cooldown,
            ::ClientboundCooldownPacket
        )
    }
}