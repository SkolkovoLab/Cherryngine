package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundHorseScreenOpenPacket(
    val windowId: Int,
    val columns: Int, // How many columns of horse inventory slots exist in the GUI, 3 slots per column.
    val entityId: Int, // The "owner" entity of the GUI. The client should close the GUI if the owner entity dies or is cleared.
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundHorseScreenOpenPacket::windowId,
            StreamCodec.VAR_INT, ClientboundHorseScreenOpenPacket::columns,
            StreamCodec.INT, ClientboundHorseScreenOpenPacket::entityId,
            ::ClientboundHorseScreenOpenPacket
        )
    }
}