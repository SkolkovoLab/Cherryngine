package ru.cherryngine.lib.minecraft.network.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.FeatureFlags
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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