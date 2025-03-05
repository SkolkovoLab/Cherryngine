package ru.cherryngine.engine.core

import jakarta.inject.Singleton
import net.minestom.server.entity.damage.DamageType
import net.minestom.server.entity.metadata.animal.tameable.WolfMeta
import net.minestom.server.entity.metadata.other.PaintingMeta
import net.minestom.server.instance.block.banner.BannerPattern
import net.minestom.server.instance.block.jukebox.JukeboxSong
import net.minestom.server.item.armor.TrimMaterial
import net.minestom.server.item.armor.TrimPattern
import net.minestom.server.item.enchant.*
import net.minestom.server.item.instrument.Instrument
import net.minestom.server.message.ChatType
import net.minestom.server.registry.DynamicRegistry
import net.minestom.server.registry.Registries
import net.minestom.server.utils.nbt.BinaryTagSerializer
import net.minestom.server.world.DimensionType
import net.minestom.server.world.biome.Biome

// Need to refactor
@Singleton
class RegistriesImpl : Registries {
    private val enchantmentLevelBasedValues: DynamicRegistry<BinaryTagSerializer<out LevelBasedValue>> =
        LevelBasedValue.createDefaultRegistry()
    private val enchantmentValueEffects: DynamicRegistry<BinaryTagSerializer<out ValueEffect>> =
        ValueEffect.createDefaultRegistry()
    private val enchantmentEntityEffects: DynamicRegistry<BinaryTagSerializer<out EntityEffect>> =
        EntityEffect.createDefaultRegistry()
    private val enchantmentLocationEffects: DynamicRegistry<BinaryTagSerializer<out LocationEffect>> =
        LocationEffect.createDefaultRegistry()

    private val chatType: DynamicRegistry<ChatType> = ChatType.createDefaultRegistry()
    private val dimensionType: DynamicRegistry<DimensionType> = DimensionType.createDefaultRegistry()
    private val biome: DynamicRegistry<Biome> = Biome.createDefaultRegistry()
    private val damageType: DynamicRegistry<DamageType> = DamageType.createDefaultRegistry()
    private val trimMaterial: DynamicRegistry<TrimMaterial> = TrimMaterial.createDefaultRegistry()
    private val trimPattern: DynamicRegistry<TrimPattern> = TrimPattern.createDefaultRegistry()
    private val bannerPattern: DynamicRegistry<BannerPattern> = BannerPattern.createDefaultRegistry()
    private val wolfVariant: DynamicRegistry<WolfMeta.Variant> = WolfMeta.Variant.createDefaultRegistry()
    private val enchantment: DynamicRegistry<Enchantment> = Enchantment.createDefaultRegistry(this)
    private val paintingVariant: DynamicRegistry<PaintingMeta.Variant> = PaintingMeta.Variant.createDefaultRegistry()
    private val jukeboxSong: DynamicRegistry<JukeboxSong> = JukeboxSong.createDefaultRegistry()
    private val instrument: DynamicRegistry<Instrument> = Instrument.createDefaultRegistry()

    override fun chatType() = chatType
    override fun dimensionType() = dimensionType
    override fun biome() = biome
    override fun damageType() = damageType
    override fun trimMaterial() = trimMaterial
    override fun trimPattern() = trimPattern
    override fun bannerPattern() = bannerPattern
    override fun wolfVariant() = wolfVariant
    override fun enchantment() = enchantment
    override fun paintingVariant() = paintingVariant
    override fun jukeboxSong() = jukeboxSong
    override fun instrument() = instrument

    override fun enchantmentLevelBasedValues() = enchantmentLevelBasedValues
    override fun enchantmentValueEffects() = enchantmentValueEffects
    override fun enchantmentEntityEffects() = enchantmentEntityEffects
    override fun enchantmentLocationEffects() = enchantmentLocationEffects
}