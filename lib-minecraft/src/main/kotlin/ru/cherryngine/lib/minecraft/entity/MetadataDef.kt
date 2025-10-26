package ru.cherryngine.lib.minecraft.entity

import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.rotation.QRot
import ru.cherryngine.lib.minecraft.entity.flags.*
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.types.*
import ru.cherryngine.lib.minecraft.registry.*
import ru.cherryngine.lib.minecraft.registry.registries.*
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.*

/**
 * List of all entity metadata.
 * <p>
 * Classes must be used (and not interfaces) to enforce loading order.
 */
@Suppress("unused")
abstract class MetadataDef {
    abstract val parent: MetadataDef?
    private var size = 0

    fun <T> index(
        index: Int,
        function: (T) -> Metadata.Entry<T>,
        defaultValue: T,
    ): MetaField<T> {
        val parentSize = parent?.size ?: 0
        val realIndex = parentSize + index
        size = maxOf(size, realIndex + 1)
        return MetaField(realIndex, function, defaultValue)
    }

    class MetaField<T>(
        val index: Int,
        val function: (T) -> Metadata.Entry<T>,
        val defaultValue: T,
    )

    object Entity : MetadataDef() {
        override val parent = null

        val ENTITY_FLAGS: MetaField<EntityMetaFlags> = index(0, EntityMetaFlags::metaEntry, EntityMetaFlags.DEFAULT)
        val AIR_TICKS: MetaField<Int> = index(1, Metadata::varInt, 300)
        val CUSTOM_NAME: MetaField<Component?> = index(2, Metadata::optChat, null)
        val CUSTOM_NAME_VISIBLE: MetaField<Boolean> = index(3, Metadata::boolean, false)
        val IS_SILENT: MetaField<Boolean> = index(4, Metadata::boolean, false)
        val HAS_NO_GRAVITY: MetaField<Boolean> = index(5, Metadata::boolean, false)
        val POSE: MetaField<EntityPose> = index(6, Metadata::pose, EntityPose.STANDING)
        val TICKS_FROZEN: MetaField<Int> = index(7, Metadata::varInt, 0)
    }

    object Interaction : MetadataDef() {
        override val parent = Entity

        val WIDTH: MetaField<Float> = index(0, Metadata::float, 1f)
        val HEIGHT: MetaField<Float> = index(1, Metadata::float, 1f)
        val RESPONSIVE: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object Display : MetadataDef() {
        override val parent = Entity

        val INTERPOLATION_DELAY: MetaField<Int> = index(0, Metadata::varInt, 0)
        val TRANSFORMATION_INTERPOLATION_DURATION: MetaField<Int> = index(1, Metadata::varInt, 0)
        val POSITION_ROTATION_INTERPOLATION_DURATION: MetaField<Int> = index(2, Metadata::varInt, 0)
        val TRANSLATION: MetaField<Vec3D> = index(3, Metadata::vector3, Vec3D.ZERO)
        val SCALE: MetaField<Vec3D> = index(4, Metadata::vector3, Vec3D.ONE)
        val ROTATION_LEFT: MetaField<QRot> = index(5, Metadata::quaternion, QRot.IDENTITY)
        val ROTATION_RIGHT: MetaField<QRot> = index(6, Metadata::quaternion, QRot.IDENTITY)
        val BILLBOARD_CONSTRAINTS: MetaField<Byte> = index(7, Metadata::byte, 0)
        val BRIGHTNESS_OVERRIDE: MetaField<Int> = index(8, Metadata::varInt, -1)
        val VIEW_RANGE: MetaField<Float> = index(9, Metadata::float, 1f)
        val SHADOW_RADIUS: MetaField<Float> = index(10, Metadata::float, 0f)
        val SHADOW_STRENGTH: MetaField<Float> = index(11, Metadata::float, 1f)
        val WIDTH: MetaField<Float> = index(12, Metadata::float, 0f)
        val HEIGHT: MetaField<Float> = index(13, Metadata::float, 0f)
        val GLOW_COLOR_OVERRIDE: MetaField<Int> = index(14, Metadata::varInt, -1)
    }

    object BlockDisplay : MetadataDef() {
        override val parent = Display

        val DISPLAYED_BLOCK_STATE: MetaField<Block> = index(0, Metadata::blockState, Block.AIR)
    }

    object ItemDisplay : MetadataDef() {
        override val parent = Display

        val DISPLAYED_ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
        val DISPLAY_TYPE: MetaField<ItemDisplayType> = index(1, Metadata::byteEnum, ItemDisplayType.NONE)
    }

    object TextDisplay : MetadataDef() {
        override val parent = Display

        val TEXT: MetaField<Component> = index(0, Metadata::chat, Component.empty())
        val LINE_WIDTH: MetaField<Int> = index(1, Metadata::varInt, 200)
        val BACKGROUND_COLOR: MetaField<Int> = index(2, Metadata::varInt, 0x40000000)
        val TEXT_OPACITY: MetaField<Byte> = index(3, Metadata::byte, -1)
        val TEXT_DISPLAY_FLAGS: MetaField<TextDisplayMetaFlags> = index(4, TextDisplayMetaFlags::metaEntry, TextDisplayMetaFlags.DEFAULT)
    }

    object ExperienceOrb : MetadataDef() {
        override val parent = Entity

        val VALUE: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object ThrownItemProjectile : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
    }

    object EyeOfEnder : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
    }

    object FallingBlock : MetadataDef() {
        override val parent = Entity

        val SPAWN_POSITION: MetaField<Vec3I> = index(0, Metadata::blockPosition, Vec3I.ZERO)
    }

    object AreaEffectCloud : MetadataDef() {
        override val parent = Entity

        val RADIUS: MetaField<Float> = index(0, Metadata::float, 0.5f)
        val COLOR: MetaField<Int> = index(1, Metadata::varInt, 0)
        val IGNORE_RADIUS_AND_SINGLE_POINT: MetaField<Boolean> = index(2, Metadata::boolean, false)
        val PARTICLE: MetaField<Particle> = index(3, Metadata::particle, Particles.EFFECT)
    }

    object FishingHook : MetadataDef() {
        override val parent = Entity

        val HOOKED: MetaField<Int> = index(0, Metadata::varInt, 0)
        val IS_CATCHABLE: MetaField<Boolean> = index(1, Metadata::boolean, false)
    }

    object AbstractArrow : MetadataDef() {
        override val parent = Entity

        val ARROW_FLAGS: MetaField<ArrowMetaFlags> = index(0, ArrowMetaFlags::metaEntry, ArrowMetaFlags.DEFAULT)
        val PIERCING_LEVEL: MetaField<Byte> = index(1, Metadata::byte, 0)
        val IN_GROUND: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object Arrow : MetadataDef() {
        override val parent = AbstractArrow

        val COLOR: MetaField<Int> = index(0, Metadata::varInt, -1)
    }

    object ThrownTrident : MetadataDef() {
        override val parent = AbstractArrow

        val LOYALTY_LEVEL: MetaField<Byte> = index(0, Metadata::byte, 0)
        val HAS_ENCHANTMENT_GLINT: MetaField<Boolean> = index(1, Metadata::boolean, false)
    }

    object AbstractVehicle : MetadataDef() {
        override val parent = Entity

        val SHAKING_POWER: MetaField<Int> = index(0, Metadata::varInt, 0)
        val SHAKING_DIRECTION: MetaField<Int> = index(1, Metadata::varInt, 1)
        val SHAKING_MULTIPLIER: MetaField<Float> = index(2, Metadata::float, 0f)
    }

    object Boat : MetadataDef() {
        override val parent = AbstractVehicle

        val IS_LEFT_PADDLE_TURNING: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val IS_RIGHT_PADDLE_TURNING: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val SPLASH_TIMER: MetaField<Int> = index(2, Metadata::varInt, 0)
    }

    object AbstractMinecart : MetadataDef() {
        override val parent = AbstractVehicle

        val CUSTOM_BLOCK_ID_AND_DAMAGE: MetaField<Block> = index(0, Metadata::optBlockState, Block.AIR)
        val CUSTOM_BLOCK_Y_POSITION: MetaField<Int> = index(1, Metadata::varInt, 6)
        val SHOW_CUSTOM_BLOCK: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object MinecartFurnace : MetadataDef() {
        override val parent = AbstractMinecart

        val HAS_FUEL: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object MinecartCommandBlock : MetadataDef() {
        override val parent = AbstractMinecart

        val COMMAND: MetaField<String> = index(0, Metadata::string, "false")
        val LAST_OUTPUT: MetaField<Component> = index(1, Metadata::chat, Component.empty())
    }

    object EndCrystal : MetadataDef() {
        override val parent = Entity

        val BEAM_TARGET: MetaField<Vec3I?> = index(0, Metadata::optBlockPosition, null)
        val SHOW_BOTTOM: MetaField<Boolean> = index(1, Metadata::boolean, true)
    }

    object SmartFireball : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
    }

    object Fireball : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
    }

    object WitherSkull : MetadataDef() {
        override val parent = Entity

        val IS_INVULNERABLE: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object FireworkRocketEntity : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
        val SHOOTER_ENTITY_ID: MetaField<Int?> = index(1, Metadata::optVarInt, null)
        val IS_SHOT_AT_ANGLE: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object Hanging : MetadataDef() {
        override val parent = Entity

        val DIRECTION: MetaField<Direction> = index(0, Metadata::direction, Direction.SOUTH)
    }

    object ItemFrame : MetadataDef() {
        override val parent = Hanging

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
        val ROTATION: MetaField<Int> = index(1, Metadata::varInt, 0)
    }

    object Painting : MetadataDef() {
        override val parent = Hanging

        val VARIANT: MetaField<PaintingVariant> = index(0, Metadata::paintingVariant, PaintingVariants.KEBAB)
    }

    object ItemEntity : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
    }

    object LivingEntity : MetadataDef() {
        override val parent = Entity

        val LIVING_ENTITY_FLAGS: MetaField<LivingEntityMetaFlags> = index(0, LivingEntityMetaFlags::metaEntry, LivingEntityMetaFlags.DEFAULT)
        val HEALTH: MetaField<Float> = index(1, Metadata::float, 1f)
        val POTION_EFFECT_PARTICLES: MetaField<List<Particle>> = index(2, Metadata::particleList, listOf())
        val IS_POTION_EFFECT_AMBIANT: MetaField<Boolean> = index(3, Metadata::boolean, false)
        val NUMBER_OF_ARROWS: MetaField<Int> = index(4, Metadata::varInt, 0)
        val NUMBER_OF_BEE_STINGERS: MetaField<Int> = index(5, Metadata::varInt, 0)
        val LOCATION_OF_BED: MetaField<Vec3I?> = index(6, Metadata::optBlockPosition, null)
    }

    object Player : MetadataDef() {
        override val parent = LivingEntity

        val ADDITIONAL_HEARTS: MetaField<Float> = index(0, Metadata::float, 0f)
        val SCORE: MetaField<Int> = index(1, Metadata::varInt, 0)
        val DISPLAYED_SKIN_PARTS: MetaField<DisplayedSkinParts> = index(2, Metadata::displayedSkinParts, DisplayedSkinParts.NONE)
        val MAIN_HAND: MetaField<Byte> = index(3, Metadata::byte, 1)
        val LEFT_SHOULDER_ENTITY_DATA: MetaField<BinaryTag> = index(4, Metadata::nbt, CompoundBinaryTag.empty())
        val RIGHT_SHOULDER_ENTITY_DATA: MetaField<BinaryTag> = index(5, Metadata::nbt, CompoundBinaryTag.empty())
    }

    object ArmorStand : MetadataDef() {
        override val parent = LivingEntity

        val ARMOR_STAND_FLAGS: MetaField<ArmorStandMetaFlags> = index(0, ArmorStandMetaFlags::metaEntry, ArmorStandMetaFlags.DEFAULT)
        val HEAD_ROTATION: MetaField<Vec3D> = index(1, Metadata::rotation, Vec3D.ZERO)
        val BODY_ROTATION: MetaField<Vec3D> = index(2, Metadata::rotation, Vec3D.ZERO)
        val LEFT_ARM_ROTATION: MetaField<Vec3D> = index(3, Metadata::rotation, Vec3D(-10.0, 0.0, -10.0))
        val RIGHT_ARM_ROTATION: MetaField<Vec3D> = index(4, Metadata::rotation, Vec3D(-15.0, 0.0, 10.0))
        val LEFT_LEG_ROTATION: MetaField<Vec3D> = index(5, Metadata::rotation, Vec3D(-1.0, 0.0, -1.0))
        val RIGHT_LEG_ROTATION: MetaField<Vec3D> = index(6, Metadata::rotation, Vec3D(1.0, 0.0, 1.0))
    }

    object Mob : MetadataDef() {
        override val parent = LivingEntity

        val MOB_FLAGS: MetaField<MobMetaFlags> = index(0, MobMetaFlags::metaEntry, MobMetaFlags.DEFAULT)
    }

    object Allay : MetadataDef() {
        override val parent = Mob

        val IS_DANCING: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val CAN_DUPLICATE: MetaField<Boolean> = index(1, Metadata::boolean, true)
    }

    object Armadillo : MetadataDef() {
        override val parent = Mob

        val STATE: MetaField<ArmadilloState> = index(0, Metadata::armadilloState, ArmadilloState.IDLE)
    }

    object Bat : MetadataDef() {
        override val parent = Mob

        val BAT_FLAGS: MetaField<BatMetaFlags> = index(0, BatMetaFlags::metaEntry, BatMetaFlags.DEFAULT)
    }

    object Dolphin : MetadataDef() {
        override val parent = Mob

        val TREASURE_POSITION: MetaField<Vec3I> = index(0, Metadata::blockPosition, Vec3I.ZERO)
        val HAS_FISH: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val MOISTURE_LEVEL: MetaField<Int> = index(2, Metadata::varInt, 2400)
    }

    object AbstractFish : MetadataDef() {
        override val parent = Mob

        val FROM_BUCKET: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object PufferFish : MetadataDef() {
        override val parent = AbstractFish

        val PUFF_STATE: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Salmon : MetadataDef() {
        override val parent = AbstractFish

        val SIZE: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object TropicalFish : MetadataDef() {
        override val parent = AbstractFish

        val VARIANT: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object AgeableMob : MetadataDef() {
        override val parent = Mob

        val IS_BABY: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Sniffer : MetadataDef() {
        override val parent = AgeableMob

        val STATE: MetaField<SnifferState> = index(0, Metadata::snifferState, SnifferState.IDLING)
        val DROP_SEED_AT_TICK: MetaField<Int> = index(1, Metadata::varInt, 0)
    }

    object AbstractHorse : MetadataDef() {
        override val parent = AgeableMob

        val ABSTRACT_HORSE_FLAGS: MetaField<AbstractHorseMetaFlags> = index(0, AbstractHorseMetaFlags::metaEntry, AbstractHorseMetaFlags.DEFAULT)
    }

    object Horse : MetadataDef() {
        override val parent = AbstractHorse

        val VARIANT: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Camel : MetadataDef() {
        override val parent = AbstractHorse

        val DASHING: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val LAST_POSE_CHANGE_TICK: MetaField<Long> = index(1, Metadata::varLong, 0L)
    }

    object ChestedHorse : MetadataDef() {
        override val parent = AbstractHorse

        val HAS_CHEST: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Llama : MetadataDef() {
        override val parent = ChestedHorse

        val STRENGTH: MetaField<Int> = index(0, Metadata::varInt, 0)
        val CARPET_COLOR: MetaField<Int> = index(0, Metadata::varInt, -1)
        val VARIANT: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Axolotl : MetadataDef() {
        override val parent = AgeableMob

        val VARIANT: MetaField<Int> = index(0, Metadata::varInt, 0)
        val IS_PLAYING_DEAD: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val IS_FROM_BUCKET: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object Bee : MetadataDef() {
        override val parent = AgeableMob

        val BEE_FLAGS: MetaField<BeeMetaFlags> = index(0, BeeMetaFlags::metaEntry, BeeMetaFlags.DEFAULT)
        val ANGER_TIME_TICKS: MetaField<Int> = index(1, Metadata::varInt, 0)
    }

    object GlowSquid : MetadataDef() {
        override val parent = AgeableMob

        val DARK_TICKS_REMAINING: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Fox : MetadataDef() {
        override val parent = AgeableMob

        val VARIANT: MetaField<Int> = index(0, Metadata::varInt, 0)
        val FOX_FLAGS: MetaField<FoxMetaFlags> = index(1, FoxMetaFlags::metaEntry, FoxMetaFlags.DEFAULT)
        val FIRST_UUID: MetaField<UUID?> = index(2, Metadata::optUuid, null)
        val SECOND_UUID: MetaField<UUID?> = index(3, Metadata::optUuid, null)
    }

    object Frog : MetadataDef() {
        override val parent = AgeableMob

        val VARIANT: MetaField<FrogVariant> = index(0, Metadata::frogVariant, FrogVariants.TEMPERATE)
        val TONGUE_TARGET: MetaField<Int?> = index(1, Metadata::optVarInt, 0)
    }

    object Ocelot : MetadataDef() {
        override val parent = AgeableMob

        val IS_TRUSTING: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Panda : MetadataDef() {
        override val parent = AgeableMob

        val BREED_TIMER: MetaField<Int> = index(0, Metadata::varInt, 0)
        val SNEEZE_TIMER: MetaField<Int> = index(1, Metadata::varInt, 0)
        val EAT_TIMER: MetaField<Int> = index(2, Metadata::varInt, 0)
        val MAIN_GENE: MetaField<Byte> = index(3, Metadata::byte, 0)
        val HIDDEN_GENE: MetaField<Byte> = index(4, Metadata::byte, 0)
        val PANDA_FLAGS: MetaField<PandaMetaFlags> = index(5, PandaMetaFlags::metaEntry, PandaMetaFlags.DEFAULT)
    }

    object Chicken : MetadataDef() {
        override val parent = AgeableMob

        val VARIANT: MetaField<ChickenVariant> = index(0, Metadata::chickenVariant, ChickenVariants.TEMPERATE)
    }

    object Cow : MetadataDef() {
        override val parent = AgeableMob

        val VARIANT: MetaField<CowVariant> = index(0, Metadata::cowVariant, CowVariants.TEMPERATE)
    }

    object Pig : MetadataDef() {
        override val parent = AgeableMob

        val BOOST_TIME: MetaField<Int> = index(0, Metadata::varInt, 0)
        val VARIANT: MetaField<PigVariant> = index(1, Metadata::pigVariant, PigVariants.TEMPERATE)
    }

    object Rabbit : MetadataDef() {
        override val parent = AgeableMob

        val TYPE: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Turtle : MetadataDef() {
        override val parent = AgeableMob

        val HAS_EGG: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val IS_LAYING_EGG: MetaField<Boolean> = index(1, Metadata::boolean, false)
    }

    object PolarBear : MetadataDef() {
        override val parent = AgeableMob

        val IS_STANDING_UP: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Mooshroom : MetadataDef() {
        override val parent = AgeableMob

        val VARIANT: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Hoglin : MetadataDef() {
        override val parent = AgeableMob

        val IMMUNE_ZOMBIFICATION: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Sheep : MetadataDef() {
        override val parent = AgeableMob

        val SHEEP_FLAGS: MetaField<SheepMetaFlags> = index(0, SheepMetaFlags::metaEntry, SheepMetaFlags.DEFAULT)
    }

    object Strider : MetadataDef() {
        override val parent = AgeableMob

        val FUNGUS_BOOST: MetaField<Int> = index(0, Metadata::varInt, 0)
        val IS_SHAKING: MetaField<Boolean> = index(1, Metadata::boolean, false)
    }

    object Goat : MetadataDef() {
        override val parent = AgeableMob

        val IS_SCREAMING_GOAT: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val HAS_LEFT_HORN: MetaField<Boolean> = index(1, Metadata::boolean, true)
        val HAS_RIGHT_HORN: MetaField<Boolean> = index(2, Metadata::boolean, true)
    }

    object TameableAnimal : MetadataDef() {
        override val parent = AgeableMob

        val TAMEABLE_ANIMAL_FLAGS: MetaField<TameableAnimalMetaFlags> = index(0, TameableAnimalMetaFlags::metaEntry, TameableAnimalMetaFlags.DEFAULT)
        val OWNER: MetaField<UUID?> = index(1, Metadata::optUuid, null)
    }

    object Cat : MetadataDef() {
        override val parent = TameableAnimal

        val VARIANT: MetaField<CatVariant> = index(0, Metadata::catVariant, CatVariants.BLACK)
        val IS_LYING: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val IS_RELAXED: MetaField<Boolean> = index(2, Metadata::boolean, false)
        val COLLAR_COLOR: MetaField<Int> = index(3, Metadata::varInt, 14)
    }

    object Wolf : MetadataDef() {
        override val parent = TameableAnimal

        val IS_BEGGING: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val COLLAR_COLOR: MetaField<DyeColor> = index(1, Metadata::enum, DyeColor.RED)
        val ANGER_TIME: MetaField<Int> = index(2, Metadata::varInt, 0)
        val VARIANT: MetaField<WolfVariant> = index(3, Metadata::wolfVariant, WolfVariants.PALE)
        val SOUND_VARIANT: MetaField<WolfSoundVariant> = index(4, Metadata::wolfSoundVariant, WolfSoundVariants.CLASSIC)
    }

    object Parrot : MetadataDef() {
        override val parent = TameableAnimal

        val VARIANT: MetaField<ParrotVariant> = index(0, Metadata::enum, ParrotVariant.RED_BLUE)
    }

    object AbstractVillager : MetadataDef() {
        override val parent = AgeableMob

        val HEAD_SHAKE_TIMER: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Villager : MetadataDef() {
        override val parent = AbstractVillager

        val VARIANT: MetaField<VillagerData> = index(0, Metadata::villagerData, VillagerData.DEFAULT)
    }

    object HappyGhast : MetadataDef() {
        override val parent = AgeableMob

        val IS_LEASH_HOLDER: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val STAYS_STILL: MetaField<Boolean> = index(1, Metadata::boolean, false)
    }

    object IronGolem : MetadataDef() {
        override val parent = Mob

        val IRON_GOLEM_FLAGS: MetaField<IronGolemMetaFlags> = index(0, IronGolemMetaFlags::metaEntry, IronGolemMetaFlags.DEFAULT)
    }

    object SnowGolem : MetadataDef() {
        override val parent = Mob

        val SNOW_GOLEM_FLAGS: MetaField<SnowGolemMetaFlags> = index(0, SnowGolemMetaFlags::metaEntry, SnowGolemMetaFlags.DEFAULT)
    }

    object Shulker : MetadataDef() {
        override val parent = Mob

        val ATTACH_FACE: MetaField<Direction> = index(0, Metadata::direction, Direction.DOWN)
        val SHIELD_HEIGHT: MetaField<Byte> = index(1, Metadata::byte, 0)
        val COLOR: MetaField<DyeColor?> = index(2, Metadata::shulkerColor, null)
    }

    object BasePiglin : MetadataDef() {
        override val parent = Mob

        val IMMUNE_ZOMBIFICATION: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Piglin : MetadataDef() {
        override val parent = BasePiglin

        val IS_BABY: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val IS_CHARGING_CROSSBOW: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val IS_DANCING: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object Blaze : MetadataDef() {
        override val parent = Mob

        val BLAZE_FLAGS: MetaField<BlazeMetaFlags> = index(0, BlazeMetaFlags::metaEntry, BlazeMetaFlags.DEFAULT)
    }

    object Bogged : MetadataDef() {
        override val parent = Mob

        val IS_SHEARED: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Creaking : MetadataDef() {
        override val parent = Mob

        val CAN_MOVE: MetaField<Boolean> = index(0, Metadata::boolean, true)
        val IS_ACTIVE: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val IS_TEARING_DOWN: MetaField<Boolean> = index(2, Metadata::boolean, false)
        val HOME_POS: MetaField<Vec3I?> = index(3, Metadata::optBlockPosition, null)
    }

    object Creeper : MetadataDef() {
        override val parent = Mob

        val STATE: MetaField<CreeperState> = index(0, Metadata::creeperState, CreeperState.IDLE)
        val IS_CHARGED: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val IS_IGNITED: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object Guardian : MetadataDef() {
        override val parent = Mob

        val IS_RETRACTING_SPIKES: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val TARGET_EID: MetaField<Int> = index(1, Metadata::varInt, 0)
    }

    object Raider : MetadataDef() {
        override val parent = Mob

        val IS_CELEBRATING: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Pillager : MetadataDef() {
        override val parent = Raider

        val IS_CHARGING: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object SpellcasterIllager : MetadataDef() {
        override val parent = Raider

        val SPELL: MetaField<Byte> = index(0, Metadata::byte, 0)
    }

    object Witch : MetadataDef() {
        override val parent = Raider

        val IS_DRINKING_POTION: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Spider : MetadataDef() {
        override val parent = Mob

        val SPIDER_FLAGS: MetaField<SpiderMetaFlags> = index(0, SpiderMetaFlags::metaEntry, SpiderMetaFlags.DEFAULT)
    }

    object Vex : MetadataDef() {
        override val parent = Mob

        val VEX_FLAGS: MetaField<VexMetaFlags> = index(0, VexMetaFlags::metaEntry, VexMetaFlags.DEFAULT)
    }

    object Warden : MetadataDef() {
        override val parent = Mob

        val ANGER_LEVEL: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Wither : MetadataDef() {
        override val parent = Mob

        val CENTER_HEAD_TARGET: MetaField<Int> = index(0, Metadata::varInt, 0)
        val LEFT_HEAD_TARGET: MetaField<Int> = index(1, Metadata::varInt, 0)
        val RIGHT_HEAD_TARGET: MetaField<Int> = index(2, Metadata::varInt, 0)
        val INVULNERABLE_TIME: MetaField<Int> = index(3, Metadata::varInt, 0)
    }

    object Zoglin : MetadataDef() {
        override val parent = Mob

        val IS_BABY: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Zombie : MetadataDef() {
        override val parent = Mob

        val IS_BABY: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val IS_BECOMING_DROWNED: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object ZombieVillager : MetadataDef() {
        override val parent = Mob

        val IS_CONVERTING: MetaField<Boolean> = index(0, Metadata::boolean, false)
        val VILLAGER_DATA: MetaField<VillagerData> = index(1, Metadata::villagerData, VillagerData.DEFAULT)
    }

    object Enderman : MetadataDef() {
        override val parent = Mob

        val CARRIED_BLOCK: MetaField<Block> = index(0, Metadata::optBlockState, Block.AIR)
        val IS_SCREAMING: MetaField<Boolean> = index(1, Metadata::boolean, false)
        val IS_STARING: MetaField<Boolean> = index(2, Metadata::boolean, false)
    }

    object EnderDragon : MetadataDef() {
        override val parent = Mob

        val DRAGON_PHASE: MetaField<EnderDragonPhase> = index(0, Metadata::enum, EnderDragonPhase.HOVERING_WITHOUT_AI)
    }

    object Ghast : MetadataDef() {
        override val parent = Mob

        val IS_ATTACKING: MetaField<Boolean> = index(0, Metadata::boolean, false)
    }

    object Phantom : MetadataDef() {
        override val parent = Mob

        val SIZE: MetaField<Int> = index(0, Metadata::varInt, 0)
    }

    object Slime : MetadataDef() {
        override val parent = Mob

        val SIZE: MetaField<Int> = index(0, Metadata::varInt, 1)
    }

    object PrimedTnt : MetadataDef() {
        override val parent = Entity

        val FUSE_TIME: MetaField<Int> = index(0, Metadata::varInt, 80)
        val BLOCK_STATE: MetaField<Block> = index(1, Metadata::blockState, Blocks.TNT.toBlock())
    }

    object OminousItemSpawner : MetadataDef() {
        override val parent = Entity

        val ITEM: MetaField<ItemStack> = index(0, Metadata::itemStack, ItemStack.AIR)
    }
}