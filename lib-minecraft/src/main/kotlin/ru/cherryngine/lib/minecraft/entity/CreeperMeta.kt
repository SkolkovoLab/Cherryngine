package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class CreeperMeta : MobMeta() {
    companion object : CreeperMeta()

    val STATE = index(
        MetadataEntry.Type.VAR_INT,
        State.IDLE,
        { if (it == -1) State.IDLE else State.FUSE },
        { if (it == State.IDLE) -1 else 1 }
    )
    val IS_CHARGED = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_IGNITED = index(MetadataEntry.Type.BOOLEAN, false)

    enum class State {
        IDLE,
        FUSE
    }
}