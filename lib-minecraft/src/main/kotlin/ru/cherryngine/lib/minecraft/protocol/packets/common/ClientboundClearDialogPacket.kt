package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ClientboundClearDialogPacket : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(::ClientboundClearDialogPacket)
    }
}