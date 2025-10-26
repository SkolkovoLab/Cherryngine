package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.registry.registries.PotionEffect
import ru.cherryngine.lib.minecraft.registry.registries.PotionEffectRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundUpdateMobEffectPacket(
    val entityId: Int,
    val effect: PotionEffect,
    val amplifier: Int,
    val duration: Int,
    val flags: Flags,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundUpdateMobEffectPacket::entityId,
            PotionEffectRegistry.STREAM_CODEC, ClientboundUpdateMobEffectPacket::effect,
            StreamCodec.VAR_INT, ClientboundUpdateMobEffectPacket::amplifier,
            StreamCodec.VAR_INT, ClientboundUpdateMobEffectPacket::duration,
            Flags.STREAM_CODEC, ClientboundUpdateMobEffectPacket::flags,
            ::ClientboundUpdateMobEffectPacket
        )
    }

    data class Flags(
        val ambient: Boolean = false,
        val particles: Boolean = true,
        val icon: Boolean = true,
        val blend: Boolean = false,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.byteFlags(
                0x01, Flags::ambient,
                0x02, Flags::particles,
                0x04, Flags::icon,
                0x08, Flags::blend,
                ::Flags
            )
        }
    }
}