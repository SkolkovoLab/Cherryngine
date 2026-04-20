package ru.cherryngine.lib.minecraft.registry

import net.minestom.server.codec.StructCodec
import net.minestom.server.dialog.Dialog
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.metadata.animal.ChickenVariant
import net.minestom.server.entity.metadata.animal.CowVariant
import net.minestom.server.entity.metadata.animal.FrogVariant
import net.minestom.server.entity.metadata.animal.PigVariant
import net.minestom.server.entity.metadata.animal.tameable.CatVariant
import net.minestom.server.entity.metadata.animal.tameable.WolfSoundVariant
import net.minestom.server.entity.metadata.animal.tameable.WolfVariant
import net.minestom.server.entity.metadata.other.PaintingVariant
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.banner.BannerPattern
import net.minestom.server.instance.block.jukebox.JukeboxSong
import net.minestom.server.instance.fluid.Fluid
import net.minestom.server.item.Material
import net.minestom.server.item.armor.TrimMaterial
import net.minestom.server.item.armor.TrimPattern
import net.minestom.server.item.enchant.Enchantment
import net.minestom.server.item.enchant.EntityEffect
import net.minestom.server.item.enchant.LevelBasedValue
import net.minestom.server.item.enchant.LocationEffect
import net.minestom.server.item.enchant.ValueEffect
import net.minestom.server.item.instrument.Instrument
import net.minestom.server.message.ChatType
import net.minestom.server.potion.PotionEffect
import net.minestom.server.registry.DynamicRegistry
import net.minestom.server.registry.Registries
import net.minestom.server.registry.Registry
import net.minestom.server.world.DimensionType
import net.minestom.server.world.biome.Biome
import net.minestom.server.world.timeline.Timeline

/**
 * Собственная реализация Minestom `Registries`, которую мы используем вместо полного
 * `ServerProcessImpl`. Набор динамических реестров инициализируется из дефолтных
 * Minestom-данных (`X.createDefaultRegistry()`), а статические берутся через
 * default-методы интерфейса (`Block.staticRegistry()` и т.п.).
 *
 * Порядок инициализации совпадает с `ServerProcessImpl` — timeline перед dimensionType,
 * а enchantment/dialog/dimensionType создаются с ссылкой на `this`.
 */
class CherryngineRegistries : Registries {
    private val enchantmentLevelBasedValues: DynamicRegistry<StructCodec<out LevelBasedValue>>
    private val enchantmentValueEffects: DynamicRegistry<StructCodec<out ValueEffect>>
    private val enchantmentEntityEffects: DynamicRegistry<StructCodec<out EntityEffect>>
    private val enchantmentLocationEffects: DynamicRegistry<StructCodec<out LocationEffect>>

    private val chatType: DynamicRegistry<ChatType>
    private val dialog: DynamicRegistry<Dialog>
    private val dimensionType: DynamicRegistry<DimensionType>
    private val biome: DynamicRegistry<Biome>
    private val damageType: DynamicRegistry<DamageType>
    private val trimMaterial: DynamicRegistry<TrimMaterial>
    private val trimPattern: DynamicRegistry<TrimPattern>
    private val bannerPattern: DynamicRegistry<BannerPattern>
    private val enchantment: DynamicRegistry<Enchantment>
    private val paintingVariant: DynamicRegistry<PaintingVariant>
    private val jukeboxSong: DynamicRegistry<JukeboxSong>
    private val instrument: DynamicRegistry<Instrument>
    private val wolfVariant: DynamicRegistry<WolfVariant>
    private val wolfSoundVariant: DynamicRegistry<WolfSoundVariant>
    private val catVariant: DynamicRegistry<CatVariant>
    private val chickenVariant: DynamicRegistry<ChickenVariant>
    private val cowVariant: DynamicRegistry<CowVariant>
    private val frogVariant: DynamicRegistry<FrogVariant>
    private val pigVariant: DynamicRegistry<PigVariant>
    private val zombieNautilusVariant: DynamicRegistry<net.minestom.server.entity.metadata.animal.ZombieNautilusVariant>
    private val timeline: DynamicRegistry<Timeline>

    init {
        enchantmentLevelBasedValues = LevelBasedValue.createDefaultRegistry()
        enchantmentValueEffects = ValueEffect.createDefaultRegistry()
        enchantmentEntityEffects = EntityEffect.createDefaultRegistry()
        enchantmentLocationEffects = LocationEffect.createDefaultRegistry()

        chatType = ChatType.createDefaultRegistry()
        biome = Biome.createDefaultRegistry()
        damageType = DamageType.createDefaultRegistry()
        trimMaterial = TrimMaterial.createDefaultRegistry()
        trimPattern = TrimPattern.createDefaultRegistry()
        bannerPattern = BannerPattern.createDefaultRegistry()
        paintingVariant = PaintingVariant.createDefaultRegistry()
        jukeboxSong = JukeboxSong.createDefaultRegistry()
        instrument = Instrument.createDefaultRegistry()
        wolfVariant = WolfVariant.createDefaultRegistry()
        wolfSoundVariant = WolfSoundVariant.createDefaultRegistry()
        catVariant = CatVariant.createDefaultRegistry()
        chickenVariant = ChickenVariant.createDefaultRegistry()
        cowVariant = CowVariant.createDefaultRegistry()
        frogVariant = FrogVariant.createDefaultRegistry()
        pigVariant = PigVariant.createDefaultRegistry()
        zombieNautilusVariant =
            net.minestom.server.entity.metadata.animal.ZombieNautilusVariant.createDefaultRegistry()
        timeline = Timeline.createDefaultRegistry()

        // depend on `this` — создаём в конце
        dialog = Dialog.createDefaultRegistry(this)
        enchantment = Enchantment.createDefaultRegistry(this)
        dimensionType = DimensionType.createDefaultRegistry(this)
    }

    override fun chatType(): DynamicRegistry<ChatType> = chatType
    override fun dimensionType(): DynamicRegistry<DimensionType> = dimensionType
    override fun biome(): DynamicRegistry<Biome> = biome
    override fun damageType(): DynamicRegistry<DamageType> = damageType
    override fun trimMaterial(): DynamicRegistry<TrimMaterial> = trimMaterial
    override fun trimPattern(): DynamicRegistry<TrimPattern> = trimPattern
    override fun bannerPattern(): DynamicRegistry<BannerPattern> = bannerPattern
    override fun enchantment(): DynamicRegistry<Enchantment> = enchantment
    override fun paintingVariant(): DynamicRegistry<PaintingVariant> = paintingVariant
    override fun jukeboxSong(): DynamicRegistry<JukeboxSong> = jukeboxSong
    override fun instrument(): DynamicRegistry<Instrument> = instrument
    override fun wolfVariant(): DynamicRegistry<WolfVariant> = wolfVariant
    override fun wolfSoundVariant(): DynamicRegistry<WolfSoundVariant> = wolfSoundVariant
    override fun catVariant(): DynamicRegistry<CatVariant> = catVariant
    override fun chickenVariant(): DynamicRegistry<ChickenVariant> = chickenVariant
    override fun cowVariant(): DynamicRegistry<CowVariant> = cowVariant
    override fun frogVariant(): DynamicRegistry<FrogVariant> = frogVariant
    override fun pigVariant(): DynamicRegistry<PigVariant> = pigVariant
    override fun zombieNautilusVariant() = zombieNautilusVariant
    override fun dialog(): DynamicRegistry<Dialog> = dialog
    override fun timeline(): DynamicRegistry<Timeline> = timeline

    override fun enchantmentLevelBasedValues() = enchantmentLevelBasedValues
    override fun enchantmentValueEffects() = enchantmentValueEffects
    override fun enchantmentEntityEffects() = enchantmentEntityEffects
    override fun enchantmentLocationEffects() = enchantmentLocationEffects

    // Статические реестры по умолчанию из default-методов Registries, но Kotlin требует
    // явные переопределения при наследовании от Java-интерфейса.
    override fun blocks(): Registry<Block> = Block.staticRegistry()
    override fun material(): Registry<Material> = Material.staticRegistry()
    override fun potionEffect(): Registry<PotionEffect> = PotionEffect.staticRegistry()
    override fun entityType(): Registry<EntityType> = EntityType.staticRegistry()
    override fun fluid(): Registry<Fluid> = Fluid.staticRegistry()
}
