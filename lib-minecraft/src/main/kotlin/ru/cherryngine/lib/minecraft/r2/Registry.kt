package ru.cherryngine.lib.minecraft.r2

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import ru.cherryngine.lib.minecraft.utils.toKey

interface Registry<T : Any> : Keyed {

    fun getOrNull(id: Int): T?
    fun getOrNull(key: Key): T?
    fun getOrNull(key: RegistryKey<T>): T? = getOrNull(key.key())
    fun getOrNull(key: String): T? = getOrNull(key.toKey())

    operator fun get(id: Int): T = getOrNull(id)
        ?: throw NoSuchElementException("No value found in registry '${key()}' for id=$id")

    operator fun get(key: Key): T = getOrNull(key)
        ?: throw NoSuchElementException("No value found in registry '${key()}' for key=$key")

    operator fun get(key: RegistryKey<T>): T = getOrNull(key)
        ?: throw NoSuchElementException("No value found in registry '${key()}' for registryKey=$key")

    operator fun get(key: String): T = getOrNull(key)
        ?: throw NoSuchElementException("No value found in registry '${key()}' for key='$key'")

    fun getKeyOrNull(id: Int): RegistryKey<T>?
    fun getKeyOrNull(value: T): RegistryKey<T>?
    fun getKeyOrNull(key: Key): RegistryKey<T>?

    fun getKey(id: Int): RegistryKey<T> = getKeyOrNull(id)
        ?: throw NoSuchElementException("No registry key found in registry '${key()}' for id=$id")

    fun getKey(value: T): RegistryKey<T> = getKeyOrNull(value)
        ?: throw NoSuchElementException("No registry key found in registry '${key()}' for value=$value")

    fun getKey(key: Key): RegistryKey<T> = getKeyOrNull(key)
        ?: throw NoSuchElementException("No registry key found in registry '${key()}' for key=$key")

    fun getIdOrNull(key: RegistryKey<T>): Int?

    fun getId(key: RegistryKey<T>): Int = getIdOrNull(key)
        ?: throw NoSuchElementException("No id found in registry '${key()}' for registryKey=$key")

    fun getId(value: T): Int =
        getId(getKey(value))

    val size: Int
    val keys: Collection<RegistryKey<T>>
    val values: Collection<T>
}
