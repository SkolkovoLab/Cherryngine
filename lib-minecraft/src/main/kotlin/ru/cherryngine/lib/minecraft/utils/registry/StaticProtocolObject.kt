package ru.cherryngine.lib.minecraft.utils.registry

import ru.cherryngine.lib.minecraft.utils.KeyedKt

interface StaticProtocolObject : KeyedKt {
    val id: Int
}