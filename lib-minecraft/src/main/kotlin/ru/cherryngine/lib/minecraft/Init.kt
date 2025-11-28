package ru.cherryngine.lib.minecraft

import ru.cherryngine.lib.minecraft.registry.RegistryManager
import ru.cherryngine.lib.minecraft.registry.registries.*
import ru.cherryngine.lib.minecraft.registry.registries.tags.*

object Init {
    // TODO избавиться нахуй от этой статики
    fun initRegistries() {
        SoundRegistry.initialize(RegistryManager.getStreamFromPath("registry/sound_registry.json.gz"))

        RegistryManager.register<Attribute>(AttributeRegistry)
        RegistryManager.register<RegistryBlock>(BlockRegistry)
        RegistryManager.register<EntityType>(EntityTypeRegistry)
        RegistryManager.register<DimensionType>(DimensionTypeRegistry)
        RegistryManager.register<BannerPattern>(BannerPatternRegistry)
        RegistryManager.register<DamageType>(DamageTypeRegistry)
        RegistryManager.register<JukeboxSong>(JukeboxSongRegistry)
        RegistryManager.register<TrimMaterial>(TrimMaterialRegistry)
        RegistryManager.register<TrimPattern>(TrimPatternRegistry)
        RegistryManager.register<ChatType>(ChatTypeRegistry)
        RegistryManager.register<Particle>(ParticleRegistry)
        RegistryManager.register<PaintingVariant>(PaintingVariantRegistry)
        RegistryManager.register<PotionEffect>(PotionEffectRegistry)
        RegistryManager.register<Biome>(BiomeRegistry)
        RegistryManager.register<Item>(ItemRegistry)
        RegistryManager.register<Fluid>(FluidRegistry)
        RegistryManager.register<PotionType>(PotionTypeRegistry)

        RegistryManager.register<WolfVariant>(WolfVariantRegistry)
        RegistryManager.register<WolfSoundVariant>(WolfSoundVariantRegistry)
        RegistryManager.register<CatVariant>(CatVariantRegistry)
        RegistryManager.register<CowVariant>(CowVariantRegistry)
        RegistryManager.register<PigVariant>(PigVariantRegistry)
        RegistryManager.register<FrogVariant>(FrogVariantRegistry)
        RegistryManager.register<ChickenVariant>(ChickenVariantRegistry)

        RegistryManager.register<Tag>(ItemTagRegistry)
        RegistryManager.register<Tag>(BlockTagRegistry)
        RegistryManager.register<Tag>(EntityTypeTagRegistry)
        RegistryManager.register<Tag>(FluidTagRegistry)
        RegistryManager.register<Tag>(BiomeTagRegistry)

        RegistryManager.register<DialogType>(DialogTypeRegistry)
        RegistryManager.register<DialogBodyType>(DialogBodyTypeRegistry)
        RegistryManager.register<DialogEntry>(DialogRegistry)
        RegistryManager.register<DialogInputType>(DialogInputTypeRegistry)
        RegistryManager.register<DialogActionType>(DialogActionTypeRegistry)
    }
}