package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import ru.cherryngine.lib.minecraft.tide.codec.Codec

enum class EntityActivity : Keyed {
    CORE,
    IDLE,
    WORK,
    PLAY,
    REST,
    MEET,
    PANIC,
    RAID,
    PRE_RAID,
    HIDE,
    FIGHT,
    CELEBRATE,
    ADMIRE_ITEM,
    AVOID,
    RIDE,
    PLAY_DEAD,
    LONG_JUMP,
    RAM,
    TONGUE,
    SWIM,
    LAY_SPAWN,
    SNIFF,
    INVESTIGATE,
    ROAR,
    EMERGE,
    DIG;

    private val key = Key.key(name.lowercase())
    override fun key(): Key = key

    companion object {
        private val BY_KEY = entries.associateBy { it.key }

        fun byKey(key: Key) = BY_KEY[key]!!

        val CODEC: Codec<EntityActivity> = Codec.KEY.transform(::byKey, Keyed::key)
    }
}