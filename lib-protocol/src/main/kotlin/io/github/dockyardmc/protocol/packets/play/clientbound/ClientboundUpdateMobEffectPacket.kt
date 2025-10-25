package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.registry.registries.PotionEffect
import io.github.dockyardmc.registry.registries.PotionEffectRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundUpdateMobEffectPacket(
    val entityId: Int,
    val effect: PotionEffect,
    val amplifier: Int,
    val duration: Int,
    val flags: Flags
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundUpdateMobEffectPacket::entityId,
            RegistryStreamCodec(PotionEffectRegistry), ClientboundUpdateMobEffectPacket::effect,
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