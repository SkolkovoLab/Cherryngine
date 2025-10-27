package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ArmadilloMeta : MobMeta() {
    companion object : ArmadilloMeta()

    val STATE = index(MetadataEntry.Type.ARMADILLO_STATE, State.IDLE)

    enum class State {
        IDLE,
        ROLLING,
        SCARED,
        UNROLLING;
    }
}