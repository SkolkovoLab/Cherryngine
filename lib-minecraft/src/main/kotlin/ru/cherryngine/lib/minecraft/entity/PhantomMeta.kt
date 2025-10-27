package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PhantomMeta : MobMeta() {
    companion object : PhantomMeta()

    val SIZE = index(MetadataEntry.Type.VAR_INT, 0)
}