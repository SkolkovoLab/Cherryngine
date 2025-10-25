package ru.cherryngine.lib.minecraft.protocol.packets.configurations

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.FeatureFlags
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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