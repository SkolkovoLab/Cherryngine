package ru.cherryngine.lib.minecraft.r2

import net.kyori.adventure.key.Keyed

interface StaticProtocolObject : Keyed {
    val id: Int
}