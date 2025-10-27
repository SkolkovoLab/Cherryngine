package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class SnifferMeta : AgeableMobMeta() {
    companion object : SnifferMeta()

    val STATE = index(MetadataEntry.Type.SNIFFER_STATE, State.IDLING)
    val DROP_SEED_AT_TICK = index(MetadataEntry.Type.VAR_INT, 0)

    enum class State {
        IDLING,
        FEELING_HAPPY,
        SCENTING,
        SNIFFING,
        SEARCHING,
        DIGGING,
        RISING;
    }
}