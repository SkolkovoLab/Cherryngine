package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.Blocks

@Suppress("PropertyName")
sealed class PrimedTntMeta : EntityMeta() {
    companion object : PrimedTntMeta()

    val FUSE_TIME = index(MetadataEntry.Type.VAR_INT, 80)
    val BLOCK_STATE = index(MetadataEntry.Type.BLOCK_STATE, Registries.block[Blocks.TNT].toBlock())
}