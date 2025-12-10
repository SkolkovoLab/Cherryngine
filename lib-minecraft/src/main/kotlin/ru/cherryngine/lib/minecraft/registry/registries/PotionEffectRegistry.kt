package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PotionEffect

object PotionEffectRegistry : DataDrivenRegistry<PotionEffect>(
    "minecraft:potion_effect",
    "potion_effect.json",
    PotionEffect.serializer()
)
