package ru.cherryngine.lib.minecraft.r2

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.KeyedKt
import ru.cherryngine.lib.minecraft.utils.toKey

class TagKey<T>(
    override val key: Key,
) : KeyedKt {
    override fun toString(): String {
        return "#$key"
    }

    companion object {
        fun <T> fromString(string: String): TagKey<T> {
            require(string.startsWith('#'))
            return TagKey(string.substring(1).toKey())
        }
    }
}