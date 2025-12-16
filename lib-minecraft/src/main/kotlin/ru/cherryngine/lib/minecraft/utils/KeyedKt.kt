package ru.cherryngine.lib.minecraft.utils

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed

interface KeyedKt : Keyed {
    val key: Key

    override fun key() = key
}