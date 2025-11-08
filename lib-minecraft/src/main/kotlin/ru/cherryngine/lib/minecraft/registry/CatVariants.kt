package ru.cherryngine.lib.minecraft.registry

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.registries.CatVariant

object CatVariants {
    val ALL_BLACK = RegistryKey<CatVariant>(Key.key("minecraft:all_black"))
    val BLACK = RegistryKey<CatVariant>(Key.key("minecraft:black"))
    val BRITISH_SHORTHAIR = RegistryKey<CatVariant>(Key.key("minecraft:british_shorthair"))
    val CALICO = RegistryKey<CatVariant>(Key.key("minecraft:calico"))
    val JELLIE = RegistryKey<CatVariant>(Key.key("minecraft:jellie"))
    val PERSIAN = RegistryKey<CatVariant>(Key.key("minecraft:persian"))
    val RAGDOLL = RegistryKey<CatVariant>(Key.key("minecraft:ragdoll"))
    val RED = RegistryKey<CatVariant>(Key.key("minecraft:red"))
    val SIAMESE = RegistryKey<CatVariant>(Key.key("minecraft:siamese"))
    val TABBY = RegistryKey<CatVariant>(Key.key("minecraft:tabby"))
    val WHITE = RegistryKey<CatVariant>(Key.key("minecraft:white"))
}
