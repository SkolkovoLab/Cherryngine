package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.PotionEffect

data class ClientboundRemoveMobEffectPacket(
    val entityId: Int,
    val effect: PotionEffect,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundRemoveMobEffectPacket::entityId,
            Registries.potionEffect.streamCodec, ClientboundRemoveMobEffectPacket::effect,
            ::ClientboundRemoveMobEffectPacket
        )
    }
}