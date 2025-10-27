package ru.cherryngine.lib.minecraft.entity

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.math.rotation.QRot
import ru.cherryngine.lib.minecraft.entity.flags.*
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.protocol.types.*
import ru.cherryngine.lib.minecraft.registry.*
import ru.cherryngine.lib.minecraft.world.block.Block

/**
 * List of all entity metadata.
 * <p>
 * Classes must be used (and not interfaces) to enforce loading order.
 */
@Suppress("unused")
sealed class MetadataDef {
    private var lastIndex = 0

    fun <T> index(
        function: MetadataEntry.Type<T>,
        defaultValue: T,
    ): MetaField<T, T> {
        return MetaField(lastIndex++, function, defaultValue, { it }, { it })
    }

    fun <T, K> index(
        function: MetadataEntry.Type<T>,
        defaultValue: K,
        mapper1: (T) -> K,
        mapper2: (K) -> T,
    ): MetaField<T, K> {
        return MetaField(lastIndex++, function, defaultValue, mapper1, mapper2)
    }

    class MetaField<T, K>(
        val index: Int,
        val function: MetadataEntry.Type<T>,
        val defaultValue: K,
        val mapper1: (T) -> K,
        val mapper2: (K) -> T,
    )

    sealed class Entity: MetadataDef() {
        companion object : Entity()

        val ENTITY_FLAGS =
            index(MetadataEntry.Type.BYTE, EntityMetaFlags.DEFAULT, EntityMetaFlags::fromByte, EntityMetaFlags::toByte)
        val AIR_TICKS = index(MetadataEntry.Type.VAR_INT, 300)
        val CUSTOM_NAME = index(MetadataEntry.Type.OPT_COMPONENT, null)
        val CUSTOM_NAME_VISIBLE = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_SILENT = index(MetadataEntry.Type.BOOLEAN, false)
        val HAS_NO_GRAVITY = index(MetadataEntry.Type.BOOLEAN, false)
        val POSE = index(MetadataEntry.Type.ENTITY_POSE, EntityPose.STANDING)
        val TICKS_FROZEN = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Interaction : Entity() {
        companion object : Interaction()

        val WIDTH = index(MetadataEntry.Type.FLOAT, 1f)
        val HEIGHT = index(MetadataEntry.Type.FLOAT, 1f)
        val RESPONSIVE = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Display : Entity() {
        companion object : Display()

        val INTERPOLATION_DELAY = index(MetadataEntry.Type.VAR_INT, 0)
        val TRANSFORMATION_INTERPOLATION_DURATION = index(MetadataEntry.Type.VAR_INT, 0)
        val POSITION_ROTATION_INTERPOLATION_DURATION = index(MetadataEntry.Type.VAR_INT, 0)
        val TRANSLATION = index(MetadataEntry.Type.VECTOR3, Vec3D.ZERO)
        val SCALE = index(MetadataEntry.Type.VECTOR3, Vec3D.ONE)
        val ROTATION_LEFT = index(MetadataEntry.Type.QUATERNION, QRot.IDENTITY)
        val ROTATION_RIGHT = index(MetadataEntry.Type.QUATERNION, QRot.IDENTITY)
        val BILLBOARD_CONSTRAINTS = index(MetadataEntry.Type.BYTE, 0)
        val BRIGHTNESS_OVERRIDE = index(MetadataEntry.Type.VAR_INT, -1)
        val VIEW_RANGE = index(MetadataEntry.Type.FLOAT, 1f)
        val SHADOW_RADIUS = index(MetadataEntry.Type.FLOAT, 0f)
        val SHADOW_STRENGTH = index(MetadataEntry.Type.FLOAT, 1f)
        val WIDTH = index(MetadataEntry.Type.FLOAT, 0f)
        val HEIGHT = index(MetadataEntry.Type.FLOAT, 0f)
        val GLOW_COLOR_OVERRIDE = index(MetadataEntry.Type.VAR_INT, -1)
    }

    sealed class BlockDisplay : Display() {
        companion object : BlockDisplay()

        val DISPLAYED_BLOCK_STATE = index(MetadataEntry.Type.BLOCK_STATE, Block.AIR)
    }

    sealed class ItemDisplay : Display() {
        companion object : ItemDisplay()

        val DISPLAYED_ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
        val DISPLAY_TYPE = index(
            MetadataEntry.Type.BYTE,
            ItemDisplayType.NONE,
            { ItemDisplayType.entries[it.toInt()] }
        ) { it.ordinal.toByte() }
    }

    sealed class TextDisplay : Display() {
        companion object : TextDisplay()

        val TEXT = index(MetadataEntry.Type.COMPONENT, Component.empty())
        val LINE_WIDTH = index(MetadataEntry.Type.VAR_INT, 200)
        val BACKGROUND_COLOR = index(MetadataEntry.Type.VAR_INT, 0x40000000)
        val TEXT_OPACITY = index(MetadataEntry.Type.BYTE, -1)
        val TEXT_DISPLAY_FLAGS = index(
            MetadataEntry.Type.BYTE,
            TextDisplayMetaFlags.DEFAULT,
            TextDisplayMetaFlags::fromByte,
            TextDisplayMetaFlags::toByte
        )
    }

    sealed class ExperienceOrb : Entity() {
        companion object : ExperienceOrb()

        val VALUE = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class ThrownItemProjectile : Entity() {
        companion object : ThrownItemProjectile()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    }

    sealed class EyeOfEnder : Entity() {
        companion object : EyeOfEnder()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    }

    sealed class FallingBlock : Entity() {
        companion object : FallingBlock()

        val SPAWN_POSITION = index(MetadataEntry.Type.BLOCK_POSITION, Vec3I.ZERO)
    }

    sealed class AreaEffectCloud : Entity() {
        companion object : AreaEffectCloud()

        val RADIUS = index(MetadataEntry.Type.FLOAT, 0.5f)
        val COLOR = index(MetadataEntry.Type.VAR_INT, 0)
        val IGNORE_RADIUS_AND_SINGLE_POINT = index(MetadataEntry.Type.BOOLEAN, false)
        val PARTICLE = index(MetadataEntry.Type.PARTICLE, Particles.EFFECT)
    }

    sealed class FishingHook : Entity() {
        companion object : FishingHook()

        val HOOKED = index(MetadataEntry.Type.VAR_INT, 0)
        val IS_CATCHABLE = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class AbstractArrow : Entity() {
        companion object : AbstractArrow()

        val ARROW_FLAGS =
            index(MetadataEntry.Type.BYTE, ArrowMetaFlags.DEFAULT, ArrowMetaFlags::fromByte, ArrowMetaFlags::toByte)
        val PIERCING_LEVEL = index(MetadataEntry.Type.BYTE, 0)
        val IN_GROUND = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Arrow : AbstractArrow() {
        companion object : Arrow()

        val COLOR = index(MetadataEntry.Type.VAR_INT, -1)
    }

    sealed class ThrownTrident : AbstractArrow() {
        companion object : ThrownTrident()

        val LOYALTY_LEVEL = index(MetadataEntry.Type.BYTE, 0)
        val HAS_ENCHANTMENT_GLINT = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class AbstractVehicle : Entity() {
        companion object : AbstractVehicle()

        val SHAKING_POWER = index(MetadataEntry.Type.VAR_INT, 0)
        val SHAKING_DIRECTION = index(MetadataEntry.Type.VAR_INT, 1)
        val SHAKING_MULTIPLIER = index(MetadataEntry.Type.FLOAT, 0f)
    }

    sealed class Boat : AbstractVehicle() {
        companion object : Boat()

        val IS_LEFT_PADDLE_TURNING = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_RIGHT_PADDLE_TURNING = index(MetadataEntry.Type.BOOLEAN, false)
        val SPLASH_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class AbstractMinecart : AbstractVehicle() {
        companion object : AbstractMinecart()

        val CUSTOM_BLOCK_ID_AND_DAMAGE = index(MetadataEntry.Type.OPT_BLOCK_STATE, Block.AIR)
        val CUSTOM_BLOCK_Y_POSITION = index(MetadataEntry.Type.VAR_INT, 6)
        val SHOW_CUSTOM_BLOCK = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class MinecartFurnace : AbstractMinecart() {
        companion object : MinecartFurnace()

        val HAS_FUEL = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class MinecartCommandBlock : AbstractMinecart() {
        companion object : MinecartCommandBlock()

        val COMMAND = index(MetadataEntry.Type.STRING, "")
        val LAST_OUTPUT = index(MetadataEntry.Type.COMPONENT, Component.empty())
    }

    sealed class EndCrystal : Entity() {
        companion object : EndCrystal()

        val BEAM_TARGET = index(MetadataEntry.Type.OPT_BLOCK_POSITION, null)
        val SHOW_BOTTOM = index(MetadataEntry.Type.BOOLEAN, true)
    }

    sealed class SmartFireball : Entity() {
        companion object : SmartFireball()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    }

    sealed class Fireball : Entity() {
        companion object : Fireball()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    }

    sealed class WitherSkull : Entity() {
        companion object : WitherSkull()

        val IS_INVULNERABLE = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class FireworkRocketEntity : Entity() {
        companion object : FireworkRocketEntity()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
        val SHOOTER_ENTITY_ID = index(MetadataEntry.Type.OPT_VAR_INT, null)
        val IS_SHOT_AT_ANGLE = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Hanging : Entity() {
        companion object : Hanging()

        val DIRECTION = index(MetadataEntry.Type.DIRECTION, Direction.SOUTH)
    }

    sealed class ItemFrame : Hanging() {
        companion object : ItemFrame()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
        val ROTATION = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Painting : Hanging() {
        companion object : Painting()

        val VARIANT = index(MetadataEntry.Type.PAINTING_VARIANT, PaintingVariants.KEBAB)
    }

    sealed class ItemEntity : Entity() {
        companion object : ItemEntity()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    }

    sealed class LivingEntity : Entity() {
        companion object : LivingEntity()

        val LIVING_ENTITY_FLAGS = index(
            MetadataEntry.Type.BYTE,
            LivingEntityMetaFlags.DEFAULT,
            LivingEntityMetaFlags::fromByte,
            LivingEntityMetaFlags::toByte
        )
        val HEALTH = index(MetadataEntry.Type.FLOAT, 1f)
        val POTION_EFFECT_PARTICLES = index(MetadataEntry.Type.PARTICLE_LIST, listOf())
        val IS_POTION_EFFECT_AMBIANT = index(MetadataEntry.Type.BOOLEAN, false)
        val NUMBER_OF_ARROWS = index(MetadataEntry.Type.VAR_INT, 0)
        val NUMBER_OF_BEE_STINGERS = index(MetadataEntry.Type.VAR_INT, 0)
        val LOCATION_OF_BED = index(MetadataEntry.Type.OPT_BLOCK_POSITION, null)
    }

    sealed class Player : LivingEntity() {
        companion object : Player()

        val ADDITIONAL_HEARTS = index(MetadataEntry.Type.FLOAT, 0f)
        val SCORE = index(MetadataEntry.Type.VAR_INT, 0)
        val DISPLAYED_SKIN_PARTS = index(
            MetadataEntry.Type.BYTE,
            DisplayedSkinParts.NONE,
            DisplayedSkinParts::fromByte,
            DisplayedSkinParts::toByte
        )
        val MAIN_HAND = index(MetadataEntry.Type.BYTE, 1)
        val LEFT_SHOULDER_ENTITY_DATA = index(MetadataEntry.Type.NBT, CompoundBinaryTag.empty())
        val RIGHT_SHOULDER_ENTITY_DATA = index(MetadataEntry.Type.NBT, CompoundBinaryTag.empty())
    }

    sealed class ArmorStand : LivingEntity() {
        companion object : ArmorStand()

        val ARMOR_STAND_FLAGS = index(
            MetadataEntry.Type.BYTE,
            ArmorStandMetaFlags.DEFAULT,
            ArmorStandMetaFlags::fromByte,
            ArmorStandMetaFlags::toByte
        )
        val HEAD_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D.ZERO)
        val BODY_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D.ZERO)
        val LEFT_ARM_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(-10.0, 0.0, -10.0))
        val RIGHT_ARM_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(-15.0, 0.0, 10.0))
        val LEFT_LEG_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(-1.0, 0.0, -1.0))
        val RIGHT_LEG_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(1.0, 0.0, 1.0))
    }

    sealed class Mob : LivingEntity() {
        companion object : Mob()

        val MOB_FLAGS =
            index(MetadataEntry.Type.BYTE, MobMetaFlags.DEFAULT, MobMetaFlags::fromByte, MobMetaFlags::toByte)
    }

    sealed class Allay : Mob() {
        companion object : Allay()

        val IS_DANCING = index(MetadataEntry.Type.BOOLEAN, false)
        val CAN_DUPLICATE = index(MetadataEntry.Type.BOOLEAN, true)
    }

    sealed class Armadillo : Mob() {
        companion object : Armadillo()

        val STATE = index(MetadataEntry.Type.ARMADILLO_STATE, ArmadilloState.IDLE)
    }

    sealed class Bat : Mob() {
        companion object : Bat()

        val BAT_FLAGS =
            index(MetadataEntry.Type.BYTE, BatMetaFlags.DEFAULT, BatMetaFlags::fromByte, BatMetaFlags::toByte)
    }

    sealed class Dolphin : Mob() {
        companion object : Dolphin()

        val TREASURE_POSITION = index(MetadataEntry.Type.BLOCK_POSITION, Vec3I.ZERO)
        val HAS_FISH = index(MetadataEntry.Type.BOOLEAN, false)
        val MOISTURE_LEVEL = index(MetadataEntry.Type.VAR_INT, 2400)
    }

    sealed class AbstractFish : Mob() {
        companion object : AbstractFish()

        val FROM_BUCKET = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class PufferFish : AbstractFish() {
        companion object : PufferFish()

        val PUFF_STATE = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Salmon : AbstractFish() {
        companion object : Salmon()

        val SIZE = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class TropicalFish : AbstractFish() {
        companion object : TropicalFish()

        val VARIANT = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class AgeableMob : Mob() {
        companion object : AgeableMob()

        val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Sniffer : AgeableMob() {
        companion object : Sniffer()

        val STATE = index(MetadataEntry.Type.SNIFFER_STATE, SnifferState.IDLING)
        val DROP_SEED_AT_TICK = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class AbstractHorse : AgeableMob() {
        companion object : AbstractHorse()

        val ABSTRACT_HORSE_FLAGS = index(
            MetadataEntry.Type.BYTE,
            AbstractHorseMetaFlags.DEFAULT,
            AbstractHorseMetaFlags::fromByte,
            AbstractHorseMetaFlags::toByte
        )
    }

    sealed class Horse : AbstractHorse() {
        companion object : Horse()

        val VARIANT = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Camel : AbstractHorse() {
        companion object : Camel()

        val DASHING = index(MetadataEntry.Type.BOOLEAN, false)
        val LAST_POSE_CHANGE_TICK = index(MetadataEntry.Type.VAR_LONG, 0L)
    }

    sealed class ChestedHorse : AbstractHorse() {
        companion object : ChestedHorse()

        val HAS_CHEST = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Llama : ChestedHorse() {
        companion object : Llama()

        val STRENGTH = index(MetadataEntry.Type.VAR_INT, 0)
        val CARPET_COLOR = index(MetadataEntry.Type.VAR_INT, -1)
        val VARIANT = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Axolotl : AgeableMob() {
        companion object : Axolotl()

        val VARIANT = index(MetadataEntry.Type.VAR_INT, AxolotlVariant.LUCY, { AxolotlVariant.entries[it] }) { it.ordinal }
        val IS_PLAYING_DEAD = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_FROM_BUCKET = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Bee : AgeableMob() {
        companion object : Bee()

        val BEE_FLAGS =
            index(MetadataEntry.Type.BYTE, BeeMetaFlags.DEFAULT, BeeMetaFlags::fromByte, BeeMetaFlags::toByte)
        val ANGER_TIME_TICKS = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class GlowSquid : AgeableMob() {
        companion object : GlowSquid()

        val DARK_TICKS_REMAINING = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Fox : AgeableMob() {
        companion object : Fox()

        val VARIANT = index(MetadataEntry.Type.VAR_INT, 0)
        val FOX_FLAGS =
            index(MetadataEntry.Type.BYTE, FoxMetaFlags.DEFAULT, FoxMetaFlags::fromByte, FoxMetaFlags::toByte)
        val FIRST_UUID = index(MetadataEntry.Type.OPT_UUID, null)
        val SECOND_UUID = index(MetadataEntry.Type.OPT_UUID, null)
    }

    sealed class Frog : AgeableMob() {
        companion object : Frog()

        val VARIANT = index(MetadataEntry.Type.FROG_VARIANT, FrogVariants.TEMPERATE)
        val TONGUE_TARGET = index(MetadataEntry.Type.OPT_VAR_INT, 0)
    }

    sealed class Ocelot : AgeableMob() {
        companion object : Ocelot()

        val IS_TRUSTING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Panda : AgeableMob() {
        companion object : Panda()

        val BREED_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
        val SNEEZE_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
        val EAT_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
        val MAIN_GENE = index(MetadataEntry.Type.BYTE, 0)
        val HIDDEN_GENE = index(MetadataEntry.Type.BYTE, 0)
        val PANDA_FLAGS =
            index(MetadataEntry.Type.BYTE, PandaMetaFlags.DEFAULT, PandaMetaFlags::fromByte, PandaMetaFlags::toByte)
    }

    sealed class Chicken : AgeableMob() {
        companion object : Chicken()

        val VARIANT = index(MetadataEntry.Type.CHICKEN_VARIANT, ChickenVariants.TEMPERATE)
    }

    sealed class Cow : AgeableMob() {
        companion object : Cow()

        val VARIANT = index(MetadataEntry.Type.COW_VARIANT, CowVariants.TEMPERATE)
    }

    sealed class Pig : AgeableMob() {
        companion object : Pig()

        val BOOST_TIME = index(MetadataEntry.Type.VAR_INT, 0)
        val VARIANT = index(MetadataEntry.Type.PIG_VARIANT, PigVariants.TEMPERATE)
    }

    sealed class Rabbit : AgeableMob() {
        companion object : Rabbit()

        val TYPE = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Turtle : AgeableMob() {
        companion object : Turtle()

        val HAS_EGG = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_LAYING_EGG = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class PolarBear : AgeableMob() {
        companion object : PolarBear()

        val IS_STANDING_UP = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Mooshroom : AgeableMob() {
        companion object : Mooshroom()

        val VARIANT = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Hoglin : AgeableMob() {
        companion object : Hoglin()

        val IMMUNE_ZOMBIFICATION = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Sheep : AgeableMob() {
        companion object : Sheep()

        val SHEEP_FLAGS =
            index(MetadataEntry.Type.BYTE, SheepMetaFlags.DEFAULT, SheepMetaFlags::fromByte, SheepMetaFlags::toByte)
    }

    sealed class Strider : AgeableMob() {
        companion object : Strider()

        val FUNGUS_BOOST = index(MetadataEntry.Type.VAR_INT, 0)
        val IS_SHAKING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Goat : AgeableMob() {
        companion object : Goat()

        val IS_SCREAMING_GOAT = index(MetadataEntry.Type.BOOLEAN, false)
        val HAS_LEFT_HORN = index(MetadataEntry.Type.BOOLEAN, true)
        val HAS_RIGHT_HORN = index(MetadataEntry.Type.BOOLEAN, true)
    }

    sealed class TameableAnimal : AgeableMob() {
        companion object : TameableAnimal()

        val TAMEABLE_ANIMAL_FLAGS =
            index(
                MetadataEntry.Type.BYTE,
                TameableAnimalMetaFlags.DEFAULT,
                TameableAnimalMetaFlags::fromByte,
                TameableAnimalMetaFlags::toByte
            )
        val OWNER = index(MetadataEntry.Type.OPT_UUID, null)
    }

    sealed class Cat : TameableAnimal() {
        companion object : Cat()

        val VARIANT = index(MetadataEntry.Type.CAT_VARIANT, CatVariants.BLACK)
        val IS_LYING = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_RELAXED = index(MetadataEntry.Type.BOOLEAN, false)
        val COLLAR_COLOR = index(MetadataEntry.Type.VAR_INT, 14)
    }

    sealed class Wolf : TameableAnimal() {
        companion object : Wolf()

        val IS_BEGGING = index(MetadataEntry.Type.BOOLEAN, false)
        val COLLAR_COLOR = index(MetadataEntry.Type.VAR_INT, DyeColor.RED, { DyeColor.entries[it] }) { it.ordinal }
        val ANGER_TIME = index(MetadataEntry.Type.VAR_INT, 0)
        val VARIANT = index(MetadataEntry.Type.WOLF_VARIANT, WolfVariants.PALE)
        val SOUND_VARIANT = index(MetadataEntry.Type.WOLF_SOUND_VARIANT, WolfSoundVariants.CLASSIC)
    }

    sealed class Parrot : TameableAnimal() {
        companion object : Parrot()

        val VARIANT =
            index(MetadataEntry.Type.VAR_INT, ParrotVariant.RED_BLUE, { ParrotVariant.entries[it] }) { it.ordinal }
    }

    sealed class AbstractVillager : AgeableMob() {
        companion object : AbstractVillager()

        val HEAD_SHAKE_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Villager : AbstractVillager() {
        companion object : Villager()

        val VARIANT = index(MetadataEntry.Type.VILLAGER_DATA, VillagerData.DEFAULT)
    }

    sealed class HappyGhast : AgeableMob() {
        companion object : HappyGhast()

        val IS_LEASH_HOLDER = index(MetadataEntry.Type.BOOLEAN, false)
        val STAYS_STILL = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class IronGolem : Mob() {
        companion object : IronGolem()

        val IRON_GOLEM_FLAGS =
            index(
                MetadataEntry.Type.BYTE,
                IronGolemMetaFlags.DEFAULT,
                IronGolemMetaFlags::fromByte,
                IronGolemMetaFlags::toByte
            )
    }

    sealed class SnowGolem : Mob() {
        companion object : SnowGolem()

        val SNOW_GOLEM_FLAGS =
            index(
                MetadataEntry.Type.BYTE,
                SnowGolemMetaFlags.DEFAULT,
                SnowGolemMetaFlags::fromByte,
                SnowGolemMetaFlags::toByte
            )
    }

    sealed class Shulker : Mob() {
        companion object : Shulker()

        val ATTACH_FACE = index(MetadataEntry.Type.DIRECTION, Direction.DOWN)
        val SHIELD_HEIGHT = index(MetadataEntry.Type.BYTE, 0)
        val COLOR: MetaField<Byte, DyeColor?> = index(
            MetadataEntry.Type.BYTE,
            null,
            { DyeColor.entries.getOrNull(it.toInt()) }
        ) { (it?.ordinal ?: 16).toByte() }
    }

    sealed class BasePiglin : Mob() {
        companion object : BasePiglin()

        val IMMUNE_ZOMBIFICATION = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Piglin : BasePiglin() {
        companion object : Piglin()

        val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_CHARGING_CROSSBOW = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_DANCING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Blaze : Mob() {
        companion object : Blaze()

        val BLAZE_FLAGS =
            index(MetadataEntry.Type.BYTE, BlazeMetaFlags.DEFAULT, BlazeMetaFlags::fromByte, BlazeMetaFlags::toByte)
    }

    sealed class Bogged : Mob() {
        companion object : Bogged()

        val IS_SHEARED = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Creaking : Mob() {
        companion object : Creaking()

        val CAN_MOVE = index(MetadataEntry.Type.BOOLEAN, true)
        val IS_ACTIVE = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_TEARING_DOWN = index(MetadataEntry.Type.BOOLEAN, false)
        val HOME_POS = index(MetadataEntry.Type.OPT_BLOCK_POSITION, null)
    }

    sealed class Creeper : Mob() {
        companion object : Creeper()

        val STATE = index(
            MetadataEntry.Type.VAR_INT,
            CreeperState.IDLE,
            { if (it == -1) CreeperState.IDLE else CreeperState.FUSE }
        ) { if (it == CreeperState.IDLE) -1 else 1 }
        val IS_CHARGED = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_IGNITED = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Guardian : Mob() {
        companion object : Guardian()

        val IS_RETRACTING_SPIKES = index(MetadataEntry.Type.BOOLEAN, false)
        val TARGET_EID = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Raider : Mob() {
        companion object : Raider()

        val IS_CELEBRATING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Pillager : Raider() {
        companion object : Pillager()

        val IS_CHARGING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class SpellcasterIllager : Raider() {
        companion object : SpellcasterIllager()

        val SPELL = index(MetadataEntry.Type.BYTE, 0)
    }

    sealed class Witch : Raider() {
        companion object : Witch()

        val IS_DRINKING_POTION = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Spider : Mob() {
        companion object : Spider()

        val SPIDER_FLAGS =
            index(MetadataEntry.Type.BYTE, SpiderMetaFlags.DEFAULT, SpiderMetaFlags::fromByte, SpiderMetaFlags::toByte)
    }

    sealed class Vex : Mob() {
        companion object : Vex()

        val VEX_FLAGS =
            index(MetadataEntry.Type.BYTE, VexMetaFlags.DEFAULT, VexMetaFlags::fromByte, VexMetaFlags::toByte)
    }

    sealed class Warden : Mob() {
        companion object : Warden()

        val ANGER_LEVEL = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Wither : Mob() {
        companion object : Wither()

        val CENTER_HEAD_TARGET = index(MetadataEntry.Type.VAR_INT, 0)
        val LEFT_HEAD_TARGET = index(MetadataEntry.Type.VAR_INT, 0)
        val RIGHT_HEAD_TARGET = index(MetadataEntry.Type.VAR_INT, 0)
        val INVULNERABLE_TIME = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Zoglin : Mob() {
        companion object : Zoglin()

        val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Zombie : Mob() {
        companion object : Zombie()

        val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_BECOMING_DROWNED = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class ZombieVillager : Mob() {
        companion object : ZombieVillager()

        val IS_CONVERTING = index(MetadataEntry.Type.BOOLEAN, false)
        val VILLAGER_DATA = index(MetadataEntry.Type.VILLAGER_DATA, VillagerData.DEFAULT)
    }

    sealed class Enderman : Mob() {
        companion object : Enderman()

        val CARRIED_BLOCK = index(MetadataEntry.Type.OPT_BLOCK_STATE, Block.AIR)
        val IS_SCREAMING = index(MetadataEntry.Type.BOOLEAN, false)
        val IS_STARING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class EnderDragon : Mob() {
        companion object : EnderDragon()

        val DRAGON_PHASE = index(
            MetadataEntry.Type.VAR_INT,
            EnderDragonPhase.HOVERING_WITHOUT_AI,
            { EnderDragonPhase.entries[it] }
        ) { it.ordinal }
    }

    sealed class Ghast : Mob() {
        companion object : Ghast()

        val IS_ATTACKING = index(MetadataEntry.Type.BOOLEAN, false)
    }

    sealed class Phantom : Mob() {
        companion object : Phantom()

        val SIZE = index(MetadataEntry.Type.VAR_INT, 0)
    }

    sealed class Slime : Mob() {
        companion object : Slime()

        val SIZE = index(MetadataEntry.Type.VAR_INT, 1)
    }

    sealed class PrimedTnt : Entity() {
        companion object : PrimedTnt()

        val FUSE_TIME = index(MetadataEntry.Type.VAR_INT, 80)
        val BLOCK_STATE = index(MetadataEntry.Type.BLOCK_STATE, Blocks.TNT.toBlock())
    }

    sealed class OminousItemSpawner : Entity() {
        companion object : OminousItemSpawner()

        val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    }
}