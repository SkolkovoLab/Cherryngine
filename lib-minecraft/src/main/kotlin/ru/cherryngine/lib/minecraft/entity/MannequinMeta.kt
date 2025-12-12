package ru.cherryngine.lib.minecraft.entity

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.network.protocol.types.ResolvableProfile

@Suppress("PropertyName")
sealed class MannequinMeta : AvatarMeta() {
    companion object : MannequinMeta()

    val PROFILE = index(MetadataEntry.Type.RESOLVABLE_PROFILE, ResolvableProfile.EMPTY)
    val IMMOVABLE = index(MetadataEntry.Type.BOOLEAN, false)
    val DESCRIPTION = index(MetadataEntry.Type.COMPONENT, Component.translatable("entity.minecraft.mannequin.label"))
}