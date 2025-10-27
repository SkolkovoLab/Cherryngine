package ru.cherryngine.lib.minecraft.entity

import net.kyori.adventure.text.Component

@Suppress("PropertyName")
sealed class MinecartCommandBlockMeta : AbstractMinecartMeta() {
    companion object : MinecartCommandBlockMeta()

    val COMMAND = index(MetadataEntry.Type.STRING, "")
    val LAST_OUTPUT = index(MetadataEntry.Type.COMPONENT, Component.empty())
}