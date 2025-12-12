package ru.cherryngine.lib.minecraft.network.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registry

data class ClientboundRegistryDataPacket(
    val registry: Registry<*>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registry.STREAM_CODEC, ClientboundRegistryDataPacket::registry,
            ::ClientboundRegistryDataPacket
        )
    }
}