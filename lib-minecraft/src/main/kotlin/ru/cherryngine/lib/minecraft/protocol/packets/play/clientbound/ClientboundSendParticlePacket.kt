//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.math.Vec3D
//import ru.cherryngine.lib.minecraft.extentions.writeRegistryEntry
//import ru.cherryngine.lib.minecraft.location.Location
//import ru.cherryngine.lib.minecraft.location.writeLocation
//import ru.cherryngine.lib.minecraft.maths.vectors.Vector3f
//import ru.cherryngine.lib.minecraft.particles.data.ParticleData
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.registry.registries.Particle
//
//class ClientboundSendParticlePacket(
//    location: Vec3D,
//    particle: Particle,
//    offset: Vec3D,
//    speed: Float,
//    count: Int,
//    overrideLimiter: Boolean = false,
//    alwaysShow: Boolean = false,
//    particleData: ParticleData?
//) : ClientboundPacket() {
//
//    init {
//        if (particleData != null && particleData.parentParticle != particle) throw IllegalArgumentException("Particle data ${particleData::class.simpleName} is not valid for particle ${particle.identifier}")
//        if (particleData == null && ParticleData.REQUIRE_PARTICLE_DATA.contains(particle)) throw IllegalArgumentException("Particle ${particle.identifier} requires particle data")
//
//        buffer.writeBoolean(overrideLimiter)
//        buffer.writeBoolean(alwaysShow)
//        buffer.writeLocation(location)
//        offset.write(buffer)
//        buffer.writeFloat(speed)
//        buffer.writeInt(count)
//        buffer.writeRegistryEntry(particle)
//        particleData?.write(buffer)
//    }
//}