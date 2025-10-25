package io.github.dockyardmc.protocol.packets.configurations

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.Registry
import io.github.dockyardmc.tide.stream.StreamCodec

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