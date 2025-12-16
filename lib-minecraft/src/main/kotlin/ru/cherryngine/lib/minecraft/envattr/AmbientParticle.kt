package ru.cherryngine.lib.minecraft.envattr

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.registry.types.Particle

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