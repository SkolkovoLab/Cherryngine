package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PufferfishMeta : AbstractFishMeta() {
    companion object : PufferfishMeta()

    val PUFF_STATE = index<Int, State>(
        MetadataEntry.Type.VAR_INT,
        State.SMALL,
        ::fromIndex,
        ::intIndex
    )

    enum class State {
        SMALL,
        MID,
        FULL
    }
}