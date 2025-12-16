package ru.cherryngine.lib.minecraft.envattr

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec

data class EnvironmentAttribute<T>(
    val identifier: String,
    val type: EnvironmentAttributeType<T>,
    val default: T,
) {
    companion object {
        val CODEC = Codec.KEY.transform(
            { EnvironmentAttributeRegistry[it] },
            { Key.key(it.identifier) }
        )
    }
}