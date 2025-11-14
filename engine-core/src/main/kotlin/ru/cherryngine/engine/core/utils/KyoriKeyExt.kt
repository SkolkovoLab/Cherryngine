package ru.cherryngine.engine.core.utils

import net.kyori.adventure.key.Key

fun String.toKey() = Key.key(this)
fun String.toKey(namespace: String) = Key.key(namespace, this)

fun Key.withValue(operator: (it: String) -> String): Key {
    return Key.key(namespace(), operator(value()))
}

fun Key.png() = withValue { "$it.png" }
fun Key.item() = withValue { "item/$it" }

fun Key.isMinecraft() = namespace() == "minecraft"
