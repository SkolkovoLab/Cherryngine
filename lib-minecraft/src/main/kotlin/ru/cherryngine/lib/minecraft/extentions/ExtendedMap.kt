package ru.cherryngine.lib.minecraft.extentions

fun <K, V> Map<K, V>.reversed(): Map<V, K> = this.entries.associate { (k, v) -> v to k }

fun <K, V> Map<K, V>.getOrThrow(key: K): V = this[key] ?: throw kotlin.NoSuchElementException("Value with key $key is not present in this map!")

