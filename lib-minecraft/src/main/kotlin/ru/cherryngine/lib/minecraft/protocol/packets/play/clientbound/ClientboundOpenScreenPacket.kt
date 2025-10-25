package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.InventoryType
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundOpenScreenPacket(
    val windowId: Int,
    val type: InventoryType,
    val name: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundOpenScreenPacket::windowId,
            EnumStreamCodec<InventoryType>(), ClientboundOpenScreenPacket::type,
            ComponentCodecs.NBT, ClientboundOpenScreenPacket::name,
            ::ClientboundOpenScreenPacket
        )
    }
}