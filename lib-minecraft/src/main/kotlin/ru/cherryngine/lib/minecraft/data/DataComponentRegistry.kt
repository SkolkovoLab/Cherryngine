package ru.cherryngine.lib.minecraft.data

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import ru.cherryngine.lib.minecraft.data.components.*
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import kotlin.reflect.KClass

object DataComponentRegistry {

    var protocolIdCounter = 0
    val dataComponentsById = Int2ObjectOpenHashMap<KClass<out DataComponent>>()
    val dataComponentsByIdentifier = Object2ObjectOpenHashMap<String, KClass<out DataComponent>>()

    val dataComponentsByIdReversed = Object2IntOpenHashMap<KClass<out DataComponent>>()
    val dataComponentsByIdentifierReversed = Object2ObjectOpenHashMap<KClass<out DataComponent>, String>()

    val codecs: MutableMap<KClass<out DataComponent>, StreamCodec<out DataComponent>> = mutableMapOf()

    fun getIdentifierById(id: Int): String {
        return dataComponentsByIdentifierReversed.getValue(dataComponentsById.getValue(id))
    }

    fun getStreamCodec(kClass: KClass<out DataComponent>): StreamCodec<out DataComponent>? {
        return codecs[kClass]
    }

    val CUSTOM_DATA = register("minecraft:custom_data", CustomDataComponent::class, CustomDataComponent.STREAM_CODEC)
    val MAX_STACK_SIZE = register("minecraft:max_stack_size", MaxStackSizeComponent::class, MaxStackSizeComponent.STREAM_CODEC)
    val MAX_DAMAGE = register("minecraft:max_damage", MaxDamageComponent::class, MaxDamageComponent.STREAM_CODEC)
    val DAMAGE = register("minecraft:damage", DamageComponent::class, DamageComponent.STREAM_CODEC)
    val UNBREAKABLE = register("minecraft:unbreakable", UnbreakableComponent::class, UnbreakableComponent.STREAM_CODEC)
    val CUSTOM_NAME = register("minecraft:custom_name", CustomNameComponent::class, CustomNameComponent.STREAM_CODEC)
    val ITEM_NAME = register("minecraft:item_name", ItemNameComponent::class, ItemNameComponent.STREAM_CODEC)
    val ITEM_MODEL = register("minecraft:item_model", ItemModelComponent::class, ItemModelComponent.STREAM_CODEC)
    val LORE = register("minecraft:lore", LoreComponent::class, LoreComponent.STREAM_CODEC)
    val RARITY = register("minecraft:rarity", RarityComponent::class, RarityComponent.STREAM_CODEC)
    val ENCHANTMENTS = skip("minecraft:enchantments")
    val CAN_PLACE_ON = register("minecraft:can_place_on", CanPlaceOnComponent::class, CanPlaceOnComponent.STREAM_CODEC)
    val CAN_BREAK = register("minecraft:can_break", CanBreakComponent::class, CanBreakComponent.STREAM_CODEC)
    val ATTRIBUTE_MODIFIERS = skip("minecraft:attribute_modifiers")
    val CUSTOM_MODEL_DATA = register("minecraft:custom_model_data", CustomModelDataComponent::class, CustomModelDataComponent.STREAM_CODEC)
    val TOOLTIP_DISPLAY = register("minecraft:tooltip_display", TooltipDisplayComponent::class, TooltipDisplayComponent.STREAM_CODEC)
    val REPAIR_COST = register("minecraft:repair_cost", RepairCostComponent::class, RepairCostComponent.STREAM_CODEC)
    val CREATIVE_SLOT_LOCK = register("minecraft:creative_slot_lock", CreativeSlotLockComponent::class, CreativeSlotLockComponent.STREAM_CODEC)
    val ENCHANTMENT_GLINT_OVERRIDE = register("minecraft:enchantment_glint_override", EnchantmentGlintOverrideComponent::class, EnchantmentGlintOverrideComponent.STREAM_CODEC)
    val INTANGIBLE_PROJECTILE = register("minecraft:intangible_projectile", IntangibleProjectileComponent::class, IntangibleProjectileComponent.STREAM_CODEC)
    val FOOD = register("minecraft:food", FoodComponent::class, FoodComponent.STREAM_CODEC)
    val CONSUMABLE = skip("minecraft:consumable")
    val USE_REMAINDER = skip("minecraft:use_remainder")
    val USE_COOLDOWN = register("minecraft:use_cooldown", UseCooldownComponent::class, UseCooldownComponent.STREAM_CODEC)
    val DAMAGE_RESISTANT = register("minecraft:damage_resistant", DamageResistantComponent::class, DamageResistantComponent.STREAM_CODEC)
    val TOOL = register("minecraft:tool", ToolComponent::class, ToolComponent.STREAM_CODEC)
    val WEAPON = register("minecraft:weapon", WeaponComponent::class, WeaponComponent.STREAM_CODEC)
    val ENCHANTABLE = register("minecraft:enchantable", EnchantableComponent::class, EnchantableComponent.STREAM_CODEC)
    val EQUIPABBLE = register("minecraft:equippable", EquippableComponent::class, EquippableComponent.STREAM_CODEC)
    val REPAIRABLE = register("minecraft:repairable", RepairableComponent::class, RepairableComponent.STREAM_CODEC)
    val GLIDER = register("minecraft:glider", GliderComponent::class, GliderComponent.STREAM_CODEC)
    val TOOLTIP_STYLE = register("minecraft:tooltip_style", TooltipStyleComponent::class, TooltipStyleComponent.STREAM_CODEC)
    val DEATH_PROTECTION = skip("minecraft:death_protection")
    val BLOCKS_ATTACKS = register("minecraft:blocks_attacks", BlocksAttacksComponent::class, BlocksAttacksComponent.STREAM_CODEC)
    val STORED_ENCHANTMENTS = skip("minecraft:stored_enchantments")
    val DYED_COLOR = register("minecraft:dyed_color", DyedColorComponent::class, DyedColorComponent.STREAM_CODEC)
    val MAP_COLOR = register("minecraft:map_color", MapColorComponent::class, MapColorComponent.STREAM_CODEC)
    val MAP_ID = register("minecraft:map_id", MapIdComponent::class, MapIdComponent.STREAM_CODEC)
    val MAP_DECORATIONS = register("minecraft:map_decorations", MapDecorationsComponent::class, MapDecorationsComponent.STREAM_CODEC)
    val MAP_POST_PROCESSING = register("minecraft:map_post_processing", MapPostProcessing::class, MapPostProcessing.STREAM_CODEC)
    val CHARGED_PROJECTILES = skip("minecraft:charged_projectiles")
    val BUNDLE_CONTENTS = skip("minecraft:bundle_contents")
    val POTION_CONTENTS = skip("minecraft:potion_contents")
    val POTION_DURATION_SCALE = register("minecraft:potion_duration_scale", PotionDurationScaleComponent::class, PotionDurationScaleComponent.STREAM_CODEC)
    val SUSPICIOUS_STEW_EFFECTS = skip("minecraft:suspicious_stew_effects")
    val WRITABLE_BOOK_CONTENT = register("minecraft:writable_book_content", WritableBookContent::class, WritableBookContent.STREAM_CODEC)
    val WRITTEN_BOOK_CONTENT = register("minecraft:written_book_content", WrittenBookContentComponent::class, WrittenBookContentComponent.STREAM_CODEC)
    val ARMOR_TRIM = register("minecraft:armor_trim", ArmorTrimComponent::class, ArmorTrimComponent.STREAM_CODEC)
    val DEBUG_STICK_STATE = register("minecraft:debug_stick_state", DebugStickComponent::class, DebugStickComponent.STREAM_CODEC)
    val ENTITY_DATA = register("minecraft:entity_data", EntityDataComponent::class, EntityDataComponent.STREAM_CODEC)
    val BUCKET_ENTITY_DATA = register("minecraft:bucket_entity_data", BucketEntityDataComponent::class, BucketEntityDataComponent.STREAM_CODEC)
    val BLOCK_ENTITY_DATA = register("minecraft:block_entity_data", BlockEntityDataComponent::class, BlockEntityDataComponent.STREAM_CODEC)
    val INSTRUMENT = register("minecraft:instrument", InstrumentComponent::class, InstrumentComponent.STREAM_CODEC)
    val PROVIDES_TRIM_MATERIAL = register("minecraft:provides_trim_material", ProvidesTrimMaterialComponent::class, ProvidesTrimMaterialComponent.STREAM_CODEC)
    val OMINOUS_BOTTLE_AMPLIFIER = register("minecraft:ominous_battle_amplifier", OminousBattleAmplifier::class, OminousBattleAmplifier.STREAM_CODEC)
    val JUKEBOX_PLAYABLE = register("minecraft:jukebox_playable", JukeboxPlayableComponent::class, JukeboxPlayableComponent.STREAM_CODEC)
    val PROVIDES_BANNER_PATTERNS = register("minecraft:provides_banner_patterns", ProvidesBannerPatterns::class, ProvidesBannerPatterns.STREAM_CODEC)
    val RECIPES = register("minecraft:recipes", RecipesComponent::class, RecipesComponent.STREAM_CODEC)
    val LODESTONE_TRACKER = register("minecraft:lodestone_tracker", LodestoneTrackerComponent::class, LodestoneTrackerComponent.STREAM_CODEC)
    val FIREWORK_EXPLOSION = register("minecraft:firework_explosion", FireworkExplosionComponent::class, FireworkExplosionComponent.STREAM_CODEC)
    val FIREWORKS = register("minecraft:fireworks", FireworksComponent::class, FireworksComponent.STREAM_CODEC)
    val PROFILE = register("minecraft:profile", ProfileComponent::class, ProfileComponent.STREAM_CODEC)
    val BANNER_PATTERNS = register("minecraft:banner_patterns", BannerPatternsComponent::class, BannerPatternsComponent.STREAM_CODEC)
    val BASE_COLOR = register("minecraft:base_color", BaseColorComponent::class, BaseColorComponent.STREAM_CODEC)
    val POT_DECORATIONS = register("minecraft:pot_decorations", PotDecorationsComponent::class, PotDecorationsComponent.STREAM_CODEC)
    val CONTAINER = skip("minecraft:container")
    val BLOCK_STATE = register("minecraft:block_state", ItemBlockStateComponent::class, ItemBlockStateComponent.STREAM_CODEC)
    val BEES = register("minecraft:bees", BeesComponent::class, BeesComponent.STREAM_CODEC)
    val LOCK = register("minecraft:lock", LockComponent::class, LockComponent.STREAM_CODEC)
    val CONTAINER_LOOT = register("minecraft:container_loot", SeededContainerLootComponent::class, SeededContainerLootComponent.STREAM_CODEC)
    val BREAK_SOUND = register("minecraft:break_sound", BreakSoundComponent::class, BreakSoundComponent.STREAM_CODEC)

    val VILLAGER_VARIANT = register("minecraft:villager/variant", VillagerVariantComponent::class, VillagerVariantComponent.STREAM_CODEC)
    val WOLF_VARIANT = register("minecraft:wolf/variant", WolfVariantComponent::class, WolfVariantComponent.STREAM_CODEC)
    val WOLF_COLLAR = register("minecraft:wolf/collar", WolfCollarComponent::class, WolfCollarComponent.STREAM_CODEC)
    val FOX_VARIANT = register("minecraft:fox/variant", FoxVariantComponent::class, FoxVariantComponent.STREAM_CODEC)
    val SALMON_SIZE = register("minecraft:salmon/size", SalmonSizeComponent::class, SalmonSizeComponent.STREAM_CODEC)
    val PARROT_VARIANT = register("minecraft:parrot/variant", ParrotVariantComponent::class, ParrotVariantComponent.STREAM_CODEC)
    val TROPICAL_FISH_PATTERN = register("minecraft:tropical_fish/pattern", TropicalFishPatternComponent::class, TropicalFishPatternComponent.STREAM_CODEC)
    val TROPICAL_FISH_BASE_COLOR = register("minecraft:tropical_fish/base_color", TropicalFishBaseColorComponent::class, TropicalFishBaseColorComponent.STREAM_CODEC)
    val TROPICAL_FISH_PATTERN_COLOR = register("minecraft:tropical_fish/pattern_color", TropicalFishPatternColorComponent::class, TropicalFishPatternColorComponent.STREAM_CODEC)
    val MOOSHROOM_VARIANT = register("minecraft:mooshroom/variant", MooshroomVariantComponent::class, MooshroomVariantComponent.STREAM_CODEC)
    val RABBIT_VARIANT = register("minecraft:rabbit/variant", RabbitVariantComponent::class, RabbitVariantComponent.STREAM_CODEC)
    val PIG_VARIANT = register("minecraft:pig/variant", PigVariantComponent::class, PigVariantComponent.STREAM_CODEC)
    val COW_VARIANT = register("minecraft:cow/variant", CowVariantComponent::class, CowVariantComponent.STREAM_CODEC)
    val CHICKEN_VARIANT = register("minecraft:chicken/variant", ChickenVariantComponent::class, ChickenVariantComponent.STREAM_CODEC)
    val FROG_VARIANT = register("minecraft:frog/variant", FrogVariantComponent::class, FrogVariantComponent.STREAM_CODEC)
    val HORSE_VARIANT = register("minecraft:horse/variant", HorseVariantComponent::class, HorseVariantComponent.STREAM_CODEC)
    val PAINTING_VARIANT = register("minecraft:painting/variant", PaintingVariantComponent::class, PaintingVariantComponent.STREAM_CODEC)
    val LLAMA_VARIANT = register("minecraft:llama/variant", LlamaVariantComponent::class, LlamaVariantComponent.STREAM_CODEC)
    val AXOLOTL_VARIANT = register("minecraft:axolotl/variant", AxolotlVariantComponent::class, AxolotlVariantComponent.STREAM_CODEC)
    val CAT_VARIANT = register("minecraft:cat/variant", CatVariantComponent::class, CatVariantComponent.STREAM_CODEC)
    val CAT_COLLAR = register("minecraft:cat/collar", CatCollarComponent::class, CatCollarComponent.STREAM_CODEC)
    val SHEEP_COLOR = register("minecraft:sheep/color", SheepColorComponent::class, SheepColorComponent.STREAM_CODEC)
    val SHULKER_COLOR = register("minecraft:shulker/color", ShulkerColorComponent::class, ShulkerColorComponent.STREAM_CODEC)

    private fun <T : DataComponent> register(identifier: String, kclass: KClass<T>, streamCodec: StreamCodec<T>): KClass<T> {
        val protocolId = protocolIdCounter
        dataComponentsById[protocolId] = kclass
        dataComponentsByIdReversed[kclass] = protocolId

        dataComponentsByIdentifier[identifier] = kclass
        dataComponentsByIdentifierReversed[kclass] = identifier

        codecs[kclass] = streamCodec

        protocolIdCounter++
        return kclass
    }

    private fun skip(identifier: String): Any? {
        protocolIdCounter++
        return null
    }
}