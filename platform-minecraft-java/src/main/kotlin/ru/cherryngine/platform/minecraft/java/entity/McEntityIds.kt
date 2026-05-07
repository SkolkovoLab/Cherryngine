package ru.cherryngine.platform.minecraft.java.entity

import java.util.concurrent.atomic.AtomicInteger

/**
 * Глобальный счётчик entity-ID для всего, что живёт в Minecraft-protocol
 * адресном пространстве (клиентские entity ID): [McEntity], а также [ru.cherryngine.platform.minecraft.java.player.MinecraftPlayer.entityId].
 * Single source of truth — гарантирует уникальность без коллизий, в отличие
 * от Random.nextInt в дисджойнт-диапазонах.
 *
 * Стартует с 1: 0 у Minecraft-клиента иногда трактуется специально (например,
 * как "сам игрок" в legacy-логике), поэтому от него уходим. Int хватит на
 * ~2 миллиарда entity'ей за процесс — реально не упрёмся.
 */
object McEntityIds {
    private val counter = AtomicInteger(0)
    fun next(): Int = counter.incrementAndGet()
}
