package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.PotionEffect
import ru.cherryngine.lib.minecraft.registry.registries.PotionEffectRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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