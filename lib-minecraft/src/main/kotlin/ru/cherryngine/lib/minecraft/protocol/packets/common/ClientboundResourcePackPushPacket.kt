package ru.cherryngine.lib.minecraft.protocol.packets.common

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

data class ClientboundResourcePackPushPacket(
    val uuid: UUID,
    val url: String,
    val hash: String,
    val forced: Boolean,
    val promptMessage: Component?,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID, ClientboundResourcePackPushPacket::uuid,
            StreamCodec.STRING, ClientboundResourcePackPushPacket::url,
            StreamCodec.STRING, ClientboundResourcePackPushPacket::hash,
            StreamCodec.BOOLEAN, ClientboundResourcePackPushPacket::forced,
            ComponentCodecs.NBT.optional(), ClientboundResourcePackPushPacket::promptMessage,
            ::ClientboundResourcePackPushPacket
        )
    }
}