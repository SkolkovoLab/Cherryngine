package ru.cherryngine.lib.minecraft.registry

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import ru.cherryngine.lib.minecraft.dialog.Dialog
import ru.cherryngine.lib.minecraft.registry.types.*
import ru.cherryngine.lib.minecraft.utils.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.utils.registry.StaticRegistry
import ru.cherryngine.lib.minecraft.utils.toKey

object Registries {
    val attribute: StaticRegistry<Attribute> = StaticRegistry.create("attribute".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<Attribute>(JsonObject(obj))
    }, "attribute.json")
    val block = StaticRegistry.create("block".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<RegistryBlock>(JsonObject(obj))
    }, "block.json")
    val soundEvent = StaticRegistry.create("sound_event".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<SoundEvent.Builtin>(JsonObject(obj))
    }, "sound_event.json")
    val potionEffect = StaticRegistry.create("potion_effect".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<PotionEffect>(JsonObject(obj))
    }, "potion_effect.json")
    val potionType = StaticRegistry.create("potion_type".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<PotionType>(JsonObject(obj))
    }, "potion_type.json")
    val particle = StaticRegistry.create("particle".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<Particle>(JsonObject(obj))
    }, "particle.json")
    val fluid = StaticRegistry.create("fluid".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<Fluid>(JsonObject(obj))
    }, "fluid.json")
    val entityType = StaticRegistry.create("entity_type".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<EntityType>(JsonObject(obj))
    }, "entity_type.json")
    val item = StaticRegistry.create("item".toKey(), { key, value ->
        val obj = value.toMutableMap()
        obj["key"] = JsonPrimitive(key)
        Json.decodeFromJsonElement<Item>(JsonObject(obj))
    }, "item.json")


    val bannerPattern = DynamicRegistry.create(
        "banner_pattern".toKey(), BannerPattern.CODEC, "banner_pattern.json",
    )
    val biome = DynamicRegistry.create(
        "worldgen/biome".toKey(), Biome.CODEC, "worldgen/biome.json",
        // We force plains to be first because it allows convenient palette initialization.
        // Maybe worth switching to fetching plains in the palette in the future to avoid this.
        Comparator.comparingInt { if (it == "minecraft:plains") 0 else 1 },
    )
    val catVariant = DynamicRegistry.create(
        "cat_variant".toKey(), CatVariant.CODEC, "cat_variant.json",
    )
    val chatType = DynamicRegistry.create(
        "chat_type".toKey(), ChatType.CODEC, "chat_type.json",
    )
    val chickenVariant = DynamicRegistry.create(
        "chicken_variant".toKey(), ChickenVariant.CODEC, "chicken_variant.json",
    )
    val cowVariant = DynamicRegistry.create(
        "cow_variant".toKey(), CowVariant.CODEC, "cow_variant.json",
    )
    val damageType = DynamicRegistry.create(
        "damage_type".toKey(), DamageType.CODEC, "damage_type.json",
    )
    val dialog = DynamicRegistry.create(
        "dialog".toKey(), Dialog.CODEC, "dialog.json",
    )
    val dimensionType = DynamicRegistry.create(
        "dimension_type".toKey(), DimensionType.CODEC, "dimension_type.json",
    )
    val frogVariant = DynamicRegistry.create(
        "frog_variant".toKey(), FrogVariant.CODEC, "frog_variant.json",
    )
    val jukeboxSong = DynamicRegistry.create(
        "jukebox_song".toKey(), JukeboxSong.CODEC, "jukebox_song.json",
    )
    val paintingVariant = DynamicRegistry.create(
        "painting_variant".toKey(), PaintingVariant.CODEC, "painting_variant.json",
    )
    val pigVariant = DynamicRegistry.create(
        "pig_variant".toKey(), PigVariant.CODEC, "pig_variant.json",
    )
    val trimMaterial = DynamicRegistry.create(
        "trim_material".toKey(), TrimMaterial.CODEC, "trim_material.json",
    )
    val trimPattern = DynamicRegistry.create(
        "trim_pattern".toKey(), TrimPattern.CODEC, "trim_pattern.json",
    )
    val wolfSoundVariant = DynamicRegistry.create(
        "wolf_sound_variant".toKey(), WolfSoundVariant.CODEC, "wolf_sound_variant.json",
    )
    val wolfVariant = DynamicRegistry.create(
        "wolf_variant".toKey(), WolfVariant.CODEC, "wolf_variant.json",
    )
    val zombieNautilusVariant = DynamicRegistry.create(
        "zombie_nautilus_variant".toKey(), ZombieNautilusVariant.CODEC, "zombie_nautilus_variant.json",
    )

    val dynamicRegistries = listOf(
        bannerPattern,
        biome,
        catVariant,
        chatType,
        chickenVariant,
        cowVariant,
        damageType,
        dimensionType,
        frogVariant,
        jukeboxSong,
        paintingVariant,
        pigVariant,
        trimMaterial,
        trimPattern,
        wolfSoundVariant,
        wolfVariant,
        zombieNautilusVariant
    )
}