package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.LocationStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.Particle

data class ClientboundLevelParticlesPacket(
    val longDistance: Boolean, // If true, particle distance increases from 256 to 65536.
    val alwaysVisible: Boolean,
    val position: Vec3D,
    val offset: Vec3D,
    val maxSpeed: Float,
    val count: Int,
    val particle: Particle, // TODO add ParticleData
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, ClientboundLevelParticlesPacket::longDistance,
            StreamCodec.BOOLEAN, ClientboundLevelParticlesPacket::alwaysVisible,
            LocationStreamCodecs.VEC_3D, ClientboundLevelParticlesPacket::position,
            LocationStreamCodecs.VEC_3D_FLOAT, ClientboundLevelParticlesPacket::offset,
            StreamCodec.FLOAT, ClientboundLevelParticlesPacket::maxSpeed,
            StreamCodec.INT, ClientboundLevelParticlesPacket::count,
            Registries.particle.streamCodec, ClientboundLevelParticlesPacket::particle,
            ::ClientboundLevelParticlesPacket
        )
    }
}