package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PotionEffect

object PotionEffectRegistry : KtJsonDataDrivenRegistry<PotionEffect>(
    "minecraft:potion_effect",
    "potion_effect.json",
    PotionEffect.serializer()
)
