package ru.cherryngine.lib.minecraft.registry

import ru.cherryngine.lib.minecraft.registry.registries.*
import ru.cherryngine.lib.minecraft.registry.registries.tags.*

object RegistryManager {
    val dynamicRegistries: MutableMap<String, Registry<*>> = mutableMapOf()
    val registries = mutableListOf<Registry<*>>()

    init {
        sequenceOf(
            AttributeRegistry,
            BlockRegistry,
            EntityTypeRegistry,
            DimensionTypeRegistry,
            BannerPatternRegistry,
            DamageTypeRegistry,
            JukeboxSongRegistry,
            TrimMaterialRegistry,
            TrimPatternRegistry,
            ChatTypeRegistry,
            ParticleRegistry,
            PaintingVariantRegistry,
            PotionEffectRegistry,
            BiomeRegistry,
            ItemRegistry,
            FluidRegistry,
            PotionTypeRegistry,

            WolfVariantRegistry,
            WolfSoundVariantRegistry,
            CatVariantRegistry,
            CowVariantRegistry,
            PigVariantRegistry,
            FrogVariantRegistry,
            ChickenVariantRegistry,

            ItemTagRegistry,
            BlockTagRegistry,
            EntityTypeTagRegistry,
            FluidTagRegistry,
            BiomeTagRegistry,

            DialogTypeRegistry,
            DialogBodyTypeRegistry,
            DialogRegistry,
            DialogInputTypeRegistry,
            DialogActionTypeRegistry,
        ).forEach { registry ->
            registries.add(registry)
            if (registry !is TagRegistry) dynamicRegistries[registry.identifier] = registry
        }
    }


    fun <T : Registry<*>> getFromIdentifier(identifier: String): T {
        return (dynamicRegistries[identifier]
            ?: throw NoSuchElementException("Registry with identifier $identifier was not found!")) as T
    }
}