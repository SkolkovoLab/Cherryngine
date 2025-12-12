package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.PotionEffect
import ru.cherryngine.lib.minecraft.registry.registries.PotionEffectRegistry

data class ClientboundRemoveMobEffectPacket(
    val entityId: Int,
    val effect: PotionEffect,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundRemoveMobEffectPacket::entityId,
            PotionEffectRegistry.STREAM_CODEC, ClientboundRemoveMobEffectPacket::effect,
            ::ClientboundRemoveMobEffectPacket
        )
    }
}