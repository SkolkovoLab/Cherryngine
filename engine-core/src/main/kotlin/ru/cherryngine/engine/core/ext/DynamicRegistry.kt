@file:Suppress("unused")

package ru.cherryngine.engine.core.ext

import net.minestom.server.registry.DynamicRegistry

fun <T : Any> DynamicRegistry<T>.getId(value: T): Int? = getKey(value)?.let { getId(it) }
