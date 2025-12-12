package ru.cherryngine.lib.minecraft.network.protocol.packets.login

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundLoginFinishedPacket(
    val gameProfile: GameProfile
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            GameProfile.STREAM_CODEC, ClientboundLoginFinishedPacket::gameProfile,
            ::ClientboundLoginFinishedPacket
        )
    }
}