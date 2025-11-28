package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.WolfSoundVariant

object WolfSoundVariantRegistry : DataDrivenRegistry<WolfSoundVariant>(
    "minecraft:wolf_sound_variant",
    "registry/wolf_sound_variant.json.gz",
    WolfSoundVariant.serializer()
)
