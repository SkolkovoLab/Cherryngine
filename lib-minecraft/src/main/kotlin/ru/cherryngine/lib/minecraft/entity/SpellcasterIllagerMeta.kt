package ru.cherryngine.lib.minecraft.entity

sealed class SpellcasterIllagerMeta : RaiderMeta() {
    companion object : SpellcasterIllagerMeta()

    val SPELL = index<Byte, Spell>(
        MetadataEntry.Type.BYTE,
        Spell.NONE,
        ::fromIndex,
        ::byteIndex
    )

    enum class Spell() {
        NONE,
        SUMMON_VEX,
        FANGS,
        WOLOLO,
        DISAPPEAR,
        BLINDNESS;
    }
}