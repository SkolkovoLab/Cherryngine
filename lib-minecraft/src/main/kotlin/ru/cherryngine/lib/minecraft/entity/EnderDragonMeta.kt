package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class EnderDragonMeta : MobMeta() {
    companion object : EnderDragonMeta()

    val DRAGON_PHASE = index<Int, Phase>(
        MetadataEntry.Type.VAR_INT,
        Phase.HOVERING_WITHOUT_AI,
        ::fromIndex,
        ::intIndex
    )

    enum class Phase {
        CIRCLING,
        STRAFING,
        FLYING_TO_THE_PORTAL,
        LANDING_ON_THE_PORTAL,
        TAKING_OFF_FROM_THE_PORTAL,
        BREATH_ATTACK,
        LOOKING_FOR_BREATH_ATTACK_PLAYER,
        ROAR,
        CHARGING_PLAYER,
        FLYING_TO_THE_PORTAL_TO_DIE,
        HOVERING_WITHOUT_AI;
    }
}