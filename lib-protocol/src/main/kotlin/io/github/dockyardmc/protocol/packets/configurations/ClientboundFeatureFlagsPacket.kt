package io.github.dockyardmc.protocol.packets.configurations

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.FeatureFlags
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundFeatureFlagsPacket(
    val flags: List<FeatureFlags>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            FeatureFlags.STREAM_CODEC.list(), ClientboundFeatureFlagsPacket::flags,
            ::ClientboundFeatureFlagsPacket
        )
    }
}