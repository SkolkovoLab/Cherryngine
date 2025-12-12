package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.InventoryType
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundOpenScreenPacket(
    val windowId: Int,
    val type: InventoryType,
    val name: Component
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundOpenScreenPacket::windowId,
            EnumStreamCodec<InventoryType>(), ClientboundOpenScreenPacket::type,
            ComponentStreamCodecs.NBT, ClientboundOpenScreenPacket::name,
            ::ClientboundOpenScreenPacket
        )
    }
}