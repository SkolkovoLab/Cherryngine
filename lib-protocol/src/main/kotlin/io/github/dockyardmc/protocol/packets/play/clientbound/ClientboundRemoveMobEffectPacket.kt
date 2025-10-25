package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.registries.PotionEffect
import io.github.dockyardmc.registry.registries.PotionEffectRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundRemoveMobEffectPacket(
    val entityId: Int,
    val effect: PotionEffect
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundRemoveMobEffectPacket::entityId,
            RegistryStreamCodec(PotionEffectRegistry), ClientboundRemoveMobEffectPacket::effect,
            ::ClientboundRemoveMobEffectPacket
        )
    }
}