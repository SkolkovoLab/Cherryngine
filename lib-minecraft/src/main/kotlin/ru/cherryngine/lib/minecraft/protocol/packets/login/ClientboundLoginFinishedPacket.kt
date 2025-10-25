package ru.cherryngine.lib.minecraft.protocol.packets.login

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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