package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.PotionEffect

object PotionEffectRegistry : DataDrivenRegistry<PotionEffect>(
    "minecraft:potion_effect",
    "registry/potion_effect_registry.json.gz",
    PotionEffect.serializer()
)
