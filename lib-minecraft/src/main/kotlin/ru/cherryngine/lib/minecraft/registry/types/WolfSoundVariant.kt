package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class WolfSoundVariant(
    val ambientSound: Key,
    val deathSound: Key,
    val growlSound: Key,
    val hurtSound: Key,
    val pantSound: Key,
    val whineSound: Key,
) {
    companion object {
        val CODEC = StructCodec.of(
            "ambient_sound", Codec.KEY, WolfSoundVariant::ambientSound,
            "death_sound", Codec.KEY, WolfSoundVariant::deathSound,
            "growl_sound", Codec.KEY, WolfSoundVariant::growlSound,
            "hurt_sound", Codec.KEY, WolfSoundVariant::hurtSound,
            "pant_sound", Codec.KEY, WolfSoundVariant::pantSound,
            "whine_sound", Codec.KEY, WolfSoundVariant::whineSound,
            ::WolfSoundVariant
        )
    }
}