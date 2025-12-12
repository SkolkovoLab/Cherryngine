package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.WolfSoundVariant

object WolfSoundVariantRegistry : KtJsonDataDrivenRegistry<WolfSoundVariant>(
    "minecraft:wolf_sound_variant",
    "wolf_sound_variant.json",
    WolfSoundVariant.serializer()
)
