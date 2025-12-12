package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.registry.entries.Particle
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec

class AmbientParticle(
    val particle: Particle,
    val probability: Float,
) {
    companion object {
        val CODEC = StructCodec.of(
            "particle", TMP_PARTICLE_CODEC, AmbientParticle::particle,
            "probability", Codec.FLOAT, AmbientParticle::probability,
            ::AmbientParticle
        )
    }
}