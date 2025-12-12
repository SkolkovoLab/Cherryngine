package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Particle

object ParticleRegistry : KtJsonDataDrivenRegistry<Particle>(
    "minecraft:particle",
    "particle.json",
    Particle.serializer()
)
