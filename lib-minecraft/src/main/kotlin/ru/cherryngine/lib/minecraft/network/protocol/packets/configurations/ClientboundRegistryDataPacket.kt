package ru.cherryngine.lib.minecraft.network.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.registry.DynamicRegistry

data class ClientboundRegistryDataPacket(
    val registry: DynamicRegistry<*>,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            DynamicRegistry.STREAM_CODEC, ClientboundRegistryDataPacket::registry,
            ::ClientboundRegistryDataPacket
        )
    }
}