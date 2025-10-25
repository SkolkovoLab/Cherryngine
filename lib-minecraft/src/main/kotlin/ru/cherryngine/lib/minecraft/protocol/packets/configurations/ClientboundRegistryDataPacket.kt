package ru.cherryngine.lib.minecraft.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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