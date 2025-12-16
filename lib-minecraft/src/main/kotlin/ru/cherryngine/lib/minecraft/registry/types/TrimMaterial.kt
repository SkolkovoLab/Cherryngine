package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.MapCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class TrimMaterial(
    val assetName: String,
    val overrideArmorMaterials: Map<String, String>,
    val description: Component,
) {
    companion object {
        private val MAP_CODEC = MapCodec(Codec.STRING, Codec.STRING).default(mapOf())
        val CODEC = StructCodec.of(
            "asset_name", Codec.STRING, TrimMaterial::assetName,
            "override_armor_materials", MAP_CODEC, TrimMaterial::overrideArmorMaterials,
            "description", ComponentCodec, TrimMaterial::description,
            ::TrimMaterial
        )
    }
}