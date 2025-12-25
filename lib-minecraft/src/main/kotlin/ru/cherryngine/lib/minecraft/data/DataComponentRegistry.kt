package ru.cherryngine.lib.minecraft.data

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.components.*
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.utils.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.utils.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.toKey
import kotlin.reflect.KClass

object DataComponentRegistry {
    val registry = DynamicRegistry<DataComponentType<DataComponent>>("data_component".toKey())
    private val entriesByKClass =
        hashMapOf<KClass<out DataComponent>, RegistryEntry<DataComponentType<DataComponent>>>()

    fun get(type: KClass<out DataComponent>): RegistryEntry<DataComponentType<DataComponent>> {
        return entriesByKClass.computeIfAbsent(type) {
            registry.entries.first { it.value.type == type }
        }
    }

    data class DataComponentType<T : DataComponent>(
        val type: KClass<T>,
        val codec: Codec<T>,
        val streamCodec: StreamCodec<T>,
    )

    val CUSTOM_DATA = register(
        "minecraft:custom_data",
        CustomDataComponent::class,
        CustomDataComponent.CODEC,
        CustomDataComponent.STREAM_CODEC
    )
    val MAX_STACK_SIZE = register(
        "minecraft:max_stack_size",
        MaxStackSizeComponent::class,
        MaxStackSizeComponent.CODEC,
        MaxStackSizeComponent.STREAM_CODEC
    )
    val MAX_DAMAGE = register(
        "minecraft:max_damage",
        MaxDamageComponent::class,
        MaxDamageComponent.CODEC,
        MaxDamageComponent.STREAM_CODEC
    )
    val DAMAGE = register(
        "minecraft:damage",
        DamageComponent::class,
        DamageComponent.CODEC,
        DamageComponent.STREAM_CODEC
    )
    val UNBREAKABLE = register(
        "minecraft:unbreakable",
        UnbreakableComponent::class,
        UnbreakableComponent.CODEC,
        UnbreakableComponent.STREAM_CODEC
    )
    val USE_EFFECTS = skip("use_effects")
    val CUSTOM_NAME = register(
        "minecraft:custom_name",
        CustomNameComponent::class,
        CustomNameComponent.CODEC,
        CustomNameComponent.STREAM_CODEC
    )
    val MINIMUM_ATTACK_CHARGE = skip("minimum_attack_charge")
    val DAMAGE_TYPE = skip("damage_type")
    val ITEM_NAME = register(
        "minecraft:item_name",
        ItemNameComponent::class,
        ItemNameComponent.CODEC,
        ItemNameComponent.STREAM_CODEC
    )
    val ITEM_MODEL = register(
        "minecraft:item_model",
        ItemModelComponent::class,
        ItemModelComponent.CODEC,
        ItemModelComponent.STREAM_CODEC
    )
    val LORE = register(
        "minecraft:lore",
        LoreComponent::class,
        LoreComponent.CODEC,
        LoreComponent.STREAM_CODEC
    )
    val RARITY = register(
        "minecraft:rarity",
        RarityComponent::class,
        RarityComponent.CODEC,
        RarityComponent.STREAM_CODEC
    )
    val ENCHANTMENTS = skip("minecraft:enchantments")
    val CAN_PLACE_ON = register(
        "minecraft:can_place_on",
        CanPlaceOnComponent::class,
        CanPlaceOnComponent.CODEC,
        CanPlaceOnComponent.STREAM_CODEC
    )
    val CAN_BREAK = register(
        "minecraft:can_break",
        CanBreakComponent::class,
        CanBreakComponent.CODEC,
        CanBreakComponent.STREAM_CODEC
    )
    val ATTRIBUTE_MODIFIERS = skip("minecraft:attribute_modifiers")
    val CUSTOM_MODEL_DATA = register(
        "minecraft:custom_model_data",
        CustomModelDataComponent::class,
        CustomModelDataComponent.CODEC,
        CustomModelDataComponent.STREAM_CODEC
    )
    val TOOLTIP_DISPLAY = register(
        "minecraft:tooltip_display",
        TooltipDisplayComponent::class,
        TooltipDisplayComponent.CODEC,
        TooltipDisplayComponent.STREAM_CODEC
    )
    val REPAIR_COST = register(
        "minecraft:repair_cost",
        RepairCostComponent::class,
        RepairCostComponent.CODEC,
        RepairCostComponent.STREAM_CODEC
    )
    val CREATIVE_SLOT_LOCK = register(
        "minecraft:creative_slot_lock",
        CreativeSlotLockComponent::class,
        CreativeSlotLockComponent.CODEC,
        CreativeSlotLockComponent.STREAM_CODEC
    )
    val ENCHANTMENT_GLINT_OVERRIDE = register(
        "minecraft:enchantment_glint_override",
        EnchantmentGlintOverrideComponent::class,
        EnchantmentGlintOverrideComponent.CODEC,
        EnchantmentGlintOverrideComponent.STREAM_CODEC
    )
    val INTANGIBLE_PROJECTILE = register(
        "minecraft:intangible_projectile",
        IntangibleProjectileComponent::class,
        IntangibleProjectileComponent.CODEC,
        IntangibleProjectileComponent.STREAM_CODEC
    )
    val FOOD = register(
        "minecraft:food",
        FoodComponent::class,
        FoodComponent.CODEC,
        FoodComponent.STREAM_CODEC
    )
    val CONSUMABLE = skip("minecraft:consumable")
    val USE_REMAINDER = skip("minecraft:use_remainder")
    val USE_COOLDOWN = register(
        "minecraft:use_cooldown",
        UseCooldownComponent::class,
        UseCooldownComponent.CODEC,
        UseCooldownComponent.STREAM_CODEC
    )
    val DAMAGE_RESISTANT = register(
        "minecraft:damage_resistant",
        DamageResistantComponent::class,
        DamageResistantComponent.CODEC,
        DamageResistantComponent.STREAM_CODEC
    )
    val TOOL = register(
        "minecraft:tool",
        ToolComponent::class,
        ToolComponent.CODEC,
        ToolComponent.STREAM_CODEC
    )
    val WEAPON = register(
        "minecraft:weapon",
        WeaponComponent::class,
        WeaponComponent.CODEC,
        WeaponComponent.STREAM_CODEC
    )
    val ATTACK_RANGE = skip("attack_range")
    val ENCHANTABLE = register(
        "minecraft:enchantable",
        EnchantableComponent::class,
        EnchantableComponent.CODEC,
        EnchantableComponent.STREAM_CODEC
    )
    val EQUIPABLE = register(
        "minecraft:equippable",
        EquippableComponent::class,
        EquippableComponent.CODEC,
        EquippableComponent.STREAM_CODEC
    )
    val REPAIRABLE = register(
        "minecraft:repairable",
        RepairableComponent::class,
        RepairableComponent.CODEC,
        RepairableComponent.STREAM_CODEC
    )
    val GLIDER = register(
        "minecraft:glider",
        GliderComponent::class,
        GliderComponent.CODEC,
        GliderComponent.STREAM_CODEC
    )
    val TOOLTIP_STYLE = register(
        "minecraft:tooltip_style",
        TooltipStyleComponent::class,
        TooltipStyleComponent.CODEC,
        TooltipStyleComponent.STREAM_CODEC
    )
    val DEATH_PROTECTION = skip("minecraft:death_protection")
    val BLOCKS_ATTACKS = register(
        "minecraft:blocks_attacks",
        BlocksAttacksComponent::class,
        BlocksAttacksComponent.CODEC,
        BlocksAttacksComponent.STREAM_CODEC
    )
    val PIERCING_WEAPON = skip("piercing_weapon")
    val KINETIC_WEAPON = skip("kinetic_weapon")
    val SWING_ANIMATION = skip("swing_animation")
    val STORED_ENCHANTMENTS = skip("minecraft:stored_enchantments")
    val DYED_COLOR = register(
        "minecraft:dyed_color",
        DyedColorComponent::class,
        DyedColorComponent.CODEC,
        DyedColorComponent.STREAM_CODEC
    )
    val MAP_COLOR = register(
        "minecraft:map_color",
        MapColorComponent::class,
        MapColorComponent.CODEC,
        MapColorComponent.STREAM_CODEC
    )
    val MAP_ID = register(
        "minecraft:map_id",
        MapIdComponent::class,
        MapIdComponent.CODEC,
        MapIdComponent.STREAM_CODEC
    )
    val MAP_DECORATIONS = register(
        "minecraft:map_decorations",
        MapDecorationsComponent::class,
        MapDecorationsComponent.CODEC,
        MapDecorationsComponent.STREAM_CODEC
    )
    val MAP_POST_PROCESSING = register(
        "minecraft:map_post_processing",
        MapPostProcessing::class,
        MapPostProcessing.CODEC,
        MapPostProcessing.STREAM_CODEC
    )
    val CHARGED_PROJECTILES = skip("minecraft:charged_projectiles")
    val BUNDLE_CONTENTS = skip("minecraft:bundle_contents")
    val POTION_CONTENTS = skip("minecraft:potion_contents")
    val POTION_DURATION_SCALE = register(
        "minecraft:potion_duration_scale",
        PotionDurationScaleComponent::class,
        PotionDurationScaleComponent.CODEC,
        PotionDurationScaleComponent.STREAM_CODEC
    )
    val SUSPICIOUS_STEW_EFFECTS = skip("minecraft:suspicious_stew_effects")
    val WRITABLE_BOOK_CONTENT = register(
        "minecraft:writable_book_content",
        WritableBookContent::class,
        WritableBookContent.CODEC,
        WritableBookContent.STREAM_CODEC
    )
    val WRITTEN_BOOK_CONTENT = register(
        "minecraft:written_book_content",
        WrittenBookContentComponent::class,
        WrittenBookContentComponent.CODEC,
        WrittenBookContentComponent.STREAM_CODEC
    )
    val ARMOR_TRIM = register(
        "minecraft:armor_trim",
        ArmorTrimComponent::class,
        ArmorTrimComponent.CODEC,
        ArmorTrimComponent.STREAM_CODEC
    )
    val DEBUG_STICK_STATE = register(
        "minecraft:debug_stick_state",
        DebugStickComponent::class,
        DebugStickComponent.CODEC,
        DebugStickComponent.STREAM_CODEC
    )
    val ENTITY_DATA = register(
        "minecraft:entity_data",
        EntityDataComponent::class,
        EntityDataComponent.CODEC,
        EntityDataComponent.STREAM_CODEC
    )
    val BUCKET_ENTITY_DATA = register(
        "minecraft:bucket_entity_data",
        BucketEntityDataComponent::class,
        BucketEntityDataComponent.CODEC,
        BucketEntityDataComponent.STREAM_CODEC
    )
    val BLOCK_ENTITY_DATA = register(
        "minecraft:block_entity_data",
        BlockEntityDataComponent::class,
        BlockEntityDataComponent.CODEC,
        BlockEntityDataComponent.STREAM_CODEC
    )
    val INSTRUMENT = register(
        "minecraft:instrument",
        InstrumentComponent::class,
        InstrumentComponent.CODEC,
        InstrumentComponent.STREAM_CODEC
    )
    val PROVIDES_TRIM_MATERIAL = register(
        "minecraft:provides_trim_material",
        ProvidesTrimMaterialComponent::class,
        ProvidesTrimMaterialComponent.CODEC,
        ProvidesTrimMaterialComponent.STREAM_CODEC
    )
    val OMINOUS_BOTTLE_AMPLIFIER = register(
        "minecraft:ominous_battle_amplifier",
        OminousBattleAmplifier::class,
        OminousBattleAmplifier.CODEC,
        OminousBattleAmplifier.STREAM_CODEC
    )
    val JUKEBOX_PLAYABLE = register(
        "minecraft:jukebox_playable",
        JukeboxPlayableComponent::class,
        JukeboxPlayableComponent.CODEC,
        JukeboxPlayableComponent.STREAM_CODEC
    )
    val PROVIDES_BANNER_PATTERNS = register(
        "minecraft:provides_banner_patterns",
        ProvidesBannerPatterns::class,
        ProvidesBannerPatterns.CODEC,
        ProvidesBannerPatterns.STREAM_CODEC
    )
    val RECIPES = register(
        "minecraft:recipes",
        RecipesComponent::class,
        RecipesComponent.CODEC,
        RecipesComponent.STREAM_CODEC
    )
    val LODESTONE_TRACKER = register(
        "minecraft:lodestone_tracker",
        LodestoneTrackerComponent::class,
        LodestoneTrackerComponent.CODEC,
        LodestoneTrackerComponent.STREAM_CODEC
    )
    val FIREWORK_EXPLOSION = register(
        "minecraft:firework_explosion",
        FireworkExplosionComponent::class,
        FireworkExplosionComponent.CODEC,
        FireworkExplosionComponent.STREAM_CODEC
    )
    val FIREWORKS = register(
        "minecraft:fireworks",
        FireworksComponent::class,
        FireworksComponent.CODEC,
        FireworksComponent.STREAM_CODEC
    )
    val PROFILE = register(
        "minecraft:profile",
        ProfileComponent::class,
        ProfileComponent.CODEC,
        ProfileComponent.STREAM_CODEC
    )
    val BANNER_PATTERNS = register(
        "minecraft:banner_patterns",
        BannerPatternsComponent::class,
        BannerPatternsComponent.CODEC,
        BannerPatternsComponent.STREAM_CODEC
    )
    val BASE_COLOR = register(
        "minecraft:base_color",
        BaseColorComponent::class,
        BaseColorComponent.CODEC,
        BaseColorComponent.STREAM_CODEC
    )
    val POT_DECORATIONS = register(
        "minecraft:pot_decorations",
        PotDecorationsComponent::class,
        PotDecorationsComponent.CODEC,
        PotDecorationsComponent.STREAM_CODEC
    )
    val CONTAINER = skip("minecraft:container")
    val BLOCK_STATE = register(
        "minecraft:block_state",
        ItemBlockStateComponent::class,
        ItemBlockStateComponent.CODEC,
        ItemBlockStateComponent.STREAM_CODEC
    )
    val BEES = register(
        "minecraft:bees",
        BeesComponent::class,
        BeesComponent.CODEC,
        BeesComponent.STREAM_CODEC
    )
    val LOCK = register(
        "minecraft:lock",
        LockComponent::class,
        LockComponent.CODEC,
        LockComponent.STREAM_CODEC
    )
    val CONTAINER_LOOT = register(
        "minecraft:container_loot",
        SeededContainerLootComponent::class,
        SeededContainerLootComponent.CODEC,
        SeededContainerLootComponent.STREAM_CODEC
    )
    val BREAK_SOUND = register(
        "minecraft:break_sound",
        BreakSoundComponent::class,
        BreakSoundComponent.CODEC,
        BreakSoundComponent.STREAM_CODEC
    )
    val VILLAGER_VARIANT = register(
        "minecraft:villager/variant",
        VillagerVariantComponent::class,
        VillagerVariantComponent.CODEC,
        VillagerVariantComponent.STREAM_CODEC
    )
    val WOLF_VARIANT = register(
        "minecraft:wolf/variant",
        WolfVariantComponent::class,
        WolfVariantComponent.CODEC,
        WolfVariantComponent.STREAM_CODEC
    )
    val WOLF_COLLAR = register(
        "minecraft:wolf/collar",
        WolfCollarComponent::class,
        WolfCollarComponent.CODEC,
        WolfCollarComponent.STREAM_CODEC
    )
    val FOX_VARIANT = register(
        "minecraft:fox/variant",
        FoxVariantComponent::class,
        FoxVariantComponent.CODEC,
        FoxVariantComponent.STREAM_CODEC
    )
    val SALMON_SIZE = register(
        "minecraft:salmon/size",
        SalmonSizeComponent::class,
        SalmonSizeComponent.CODEC,
        SalmonSizeComponent.STREAM_CODEC
    )
    val PARROT_VARIANT = register(
        "minecraft:parrot/variant",
        ParrotVariantComponent::class,
        ParrotVariantComponent.CODEC,
        ParrotVariantComponent.STREAM_CODEC
    )
    val TROPICAL_FISH_PATTERN = register(
        "minecraft:tropical_fish/pattern",
        TropicalFishPatternComponent::class,
        TropicalFishPatternComponent.CODEC,
        TropicalFishPatternComponent.STREAM_CODEC
    )
    val TROPICAL_FISH_BASE_COLOR = register(
        "minecraft:tropical_fish/base_color",
        TropicalFishBaseColorComponent::class,
        TropicalFishBaseColorComponent.CODEC,
        TropicalFishBaseColorComponent.STREAM_CODEC
    )
    val TROPICAL_FISH_PATTERN_COLOR = register(
        "minecraft:tropical_fish/pattern_color",
        TropicalFishPatternColorComponent::class,
        TropicalFishPatternColorComponent.CODEC,
        TropicalFishPatternColorComponent.STREAM_CODEC
    )
    val MOOSHROOM_VARIANT = register(
        "minecraft:mooshroom/variant",
        MooshroomVariantComponent::class,
        MooshroomVariantComponent.CODEC,
        MooshroomVariantComponent.STREAM_CODEC
    )
    val RABBIT_VARIANT = register(
        "minecraft:rabbit/variant",
        RabbitVariantComponent::class,
        RabbitVariantComponent.CODEC,
        RabbitVariantComponent.STREAM_CODEC
    )
    val PIG_VARIANT = register(
        "minecraft:pig/variant",
        PigVariantComponent::class,
        PigVariantComponent.CODEC,
        PigVariantComponent.STREAM_CODEC
    )
    val COW_VARIANT = register(
        "minecraft:cow/variant",
        CowVariantComponent::class,
        CowVariantComponent.CODEC,
        CowVariantComponent.STREAM_CODEC
    )
    val CHICKEN_VARIANT = register(
        "minecraft:chicken/variant",
        ChickenVariantComponent::class,
        ChickenVariantComponent.CODEC,
        ChickenVariantComponent.STREAM_CODEC
    )
    val ZOMBIE_NAUTILUS_VARIANT = skip("zombie_nautilus_variant")
    val FROG_VARIANT = register(
        "minecraft:frog/variant",
        FrogVariantComponent::class,
        FrogVariantComponent.CODEC,
        FrogVariantComponent.STREAM_CODEC
    )
    val HORSE_VARIANT = register(
        "minecraft:horse/variant",
        HorseVariantComponent::class,
        HorseVariantComponent.CODEC,
        HorseVariantComponent.STREAM_CODEC
    )
    val PAINTING_VARIANT = register(
        "minecraft:painting/variant",
        PaintingVariantComponent::class,
        PaintingVariantComponent.CODEC,
        PaintingVariantComponent.STREAM_CODEC
    )
    val LLAMA_VARIANT = register(
        "minecraft:llama/variant",
        LlamaVariantComponent::class,
        LlamaVariantComponent.CODEC,
        LlamaVariantComponent.STREAM_CODEC
    )
    val AXOLOTL_VARIANT = register(
        "minecraft:axolotl/variant",
        AxolotlVariantComponent::class,
        AxolotlVariantComponent.CODEC,
        AxolotlVariantComponent.STREAM_CODEC
    )
    val CAT_VARIANT = register(
        "minecraft:cat/variant",
        CatVariantComponent::class,
        CatVariantComponent.CODEC,
        CatVariantComponent.STREAM_CODEC
    )
    val CAT_COLLAR = register(
        "minecraft:cat/collar",
        CatCollarComponent::class,
        CatCollarComponent.CODEC,
        CatCollarComponent.STREAM_CODEC
    )
    val SHEEP_COLOR = register(
        "minecraft:sheep/color",
        SheepColorComponent::class,
        SheepColorComponent.CODEC,
        SheepColorComponent.STREAM_CODEC
    )
    val SHULKER_COLOR = register(
        "minecraft:shulker/color",
        ShulkerColorComponent::class,
        ShulkerColorComponent.CODEC,
        ShulkerColorComponent.STREAM_CODEC
    )

    private fun <T : DataComponent> register(
        identifier: String,
        type: KClass<T>,
        codec: Codec<T>,
        streamCodec: StreamCodec<T>,
    ): DataComponentType<T> {
        val key = identifier.toKey()
        val dataComponentType = DataComponentType(type, codec, streamCodec)
        (registry as DynamicRegistry<DataComponentType<T>>).register(key, dataComponentType)
        return dataComponentType
    }

    private fun skip(identifier: String): Any? {
        return null
    }
}