package ru.cherryngine.lib.minecraft.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import kotlin.io.path.Path

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val keysPackage = "ru.cherryngine.lib.minecraft.registry.keys"
        val typesPackage = "ru.cherryngine.lib.minecraft.registry.types"
        generateKeys(
            "$keysPackage.Attributes",
            "$typesPackage.Attribute",
            "attribute.json"
        )
        generateKeys(
            "$keysPackage.Blocks",
            "$typesPackage.RegistryBlock",
            "block.json"
        )
        generateKeys(
            "$keysPackage.SoundEvents",
            "$typesPackage.SoundEvent.Builtin",
            "sound_event.json"
        )
        generateKeys(
            "$keysPackage.PotionEffects",
            "$typesPackage.PotionEffect",
            "potion_effect.json"
        )
        generateKeys(
            "$keysPackage.PotionTypes",
            "$typesPackage.PotionType",
            "potion_type.json"
        )
        generateKeys(
            "$keysPackage.Particles",
            "$typesPackage.Particle",
            "particle.json"
        )
        generateKeys(
            "$keysPackage.Fluids",
            "$typesPackage.Fluid",
            "fluid.json"
        )
        generateKeys(
            "$keysPackage.EntityTypes",
            "$typesPackage.EntityType",
            "entity_type.json"
        )
        generateKeys(
            "$keysPackage.Items",
            "$typesPackage.Item",
            "item.json"
        )
        generateKeys(
            "$keysPackage.BannerPatterns",
            "$typesPackage.BannerPattern",
            "banner_pattern.json"
        )
        generateKeys(
            "$keysPackage.Biomes",
            "$typesPackage.Biome",
            "worldgen/biome.json"
        )
        generateKeys(
            "$keysPackage.CatVariants",
            "$typesPackage.CatVariant",
            "cat_variant.json"
        )
        generateKeys(
            "$keysPackage.ChatTypes",
            "$typesPackage.ChatType",
            "chat_type.json"
        )
        generateKeys(
            "$keysPackage.ChickenVariants",
            "$typesPackage.ChickenVariant",
            "chicken_variant.json"
        )
        generateKeys(
            "$keysPackage.CowVariants",
            "$typesPackage.CowVariant",
            "cow_variant.json"
        )
        generateKeys(
            "$keysPackage.DamageTypes",
            "$typesPackage.DamageType",
            "damage_type.json"
        )
        generateKeys(
            "$keysPackage.Dialogs",
            "ru.cherryngine.lib.minecraft.dialog.Dialog",
            "dialog.json"
        )
        generateKeys(
            "$keysPackage.DimensionTypes",
            "$typesPackage.DimensionType",
            "dimension_type.json"
        )
        generateKeys(
            "$keysPackage.FrogVariants",
            "$typesPackage.FrogVariant",
            "frog_variant.json"
        )
        generateKeys(
            "$keysPackage.JukeboxSongs",
            "$typesPackage.JukeboxSong",
            "jukebox_song.json"
        )
        generateKeys(
            "$keysPackage.PaintingVariants",
            "$typesPackage.PaintingVariant",
            "painting_variant.json"
        )
        generateKeys(
            "$keysPackage.PigVariants",
            "$typesPackage.PigVariant",
            "pig_variant.json"
        )
        generateKeys(
            "$keysPackage.TrimMaterials",
            "$typesPackage.TrimMaterial",
            "trim_material.json"
        )
        generateKeys(
            "$keysPackage.TrimPatterns",
            "$typesPackage.TrimPattern",
            "trim_pattern.json"
        )
        generateKeys(
            "$keysPackage.WolfSoundVariants",
            "$typesPackage.WolfSoundVariant",
            "wolf_sound_variant.json"
        )
        generateKeys(
            "$keysPackage.WolfVariants",
            "$typesPackage.WolfVariant",
            "wolf_variant.json"
        )
        generateKeys(
            "$keysPackage.ZombieNautilusVariants",
            "$typesPackage.ZombieNautilusVariant",
            "zombie_nautilus_variant.json"
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun readKeys(resource: String): Set<String> {
        val resource = ClassLoader.getSystemResource(resource)!!
        val stream = resource.openStream()
        val parsed = stream.use { Json.decodeFromStream<Map<String, JsonObject>>(it) }
        return parsed.keys
    }

    fun writeFile(keyClass: ClassName, typeSpec: TypeSpec) {
        val fileSpec = FileSpec.builder(keyClass)
            .addFileComment("AUTOGENERATED. DO NOT EDIT.")
            .addType(typeSpec)
            .build()

        fileSpec.writeTo(Path("../../src/main/kotlin"))
    }

    val REGISTRY_KEY_CLASS = ClassName(
        "ru.cherryngine.lib.minecraft.utils.registry",
        "RegistryKey"
    )
    val CREATE_KEY_FUNCTION = MemberName(
        "ru.cherryngine.lib.minecraft.registry",
        "createKey"
    )

    fun generateKeys(
        keyClassName: String,
        entryClassName: String,
        resource: String,
    ) {
        val keyClass = ClassName.bestGuess(keyClassName)
        val entryClass = ClassName.bestGuess(entryClassName)
        val registryKeyType = REGISTRY_KEY_CLASS.parameterizedBy(entryClass)

        val suppressRedundantVisibility = AnnotationSpec.builder(Suppress::class)
            .addMember("%S", "RedundantVisibilityModifier")
            .build()

        val objectBuilder = TypeSpec.objectBuilder(keyClass.simpleName)
            .addAnnotation(suppressRedundantVisibility)

        readKeys(resource).forEach { key ->
            val constName = key.substringAfter(":").uppercase().replace(".", "_")

            val property = PropertySpec.builder(constName, registryKeyType)
                .initializer("%M(%S)", CREATE_KEY_FUNCTION, key)
                .build()

            objectBuilder.addProperty(property)
        }

        writeFile(keyClass, objectBuilder.build())
    }
}