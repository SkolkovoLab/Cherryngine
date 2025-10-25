package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.InventoryType
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.text.Component

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