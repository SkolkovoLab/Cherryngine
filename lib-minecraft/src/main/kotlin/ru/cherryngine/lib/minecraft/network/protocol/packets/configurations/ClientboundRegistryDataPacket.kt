package ru.cherryngine.lib.minecraft.network.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.registry.DataDrivenRegistry

data class ClientboundRegistryDataPacket(
    val registry: DataDrivenRegistry<*>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            DataDrivenRegistry.STREAM_CODEC, ClientboundRegistryDataPacket::registry,
            ::ClientboundRegistryDataPacket
        )
    }
}