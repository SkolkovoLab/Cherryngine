package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.platform.PlatformHandler

/**
 * Источник команд игрока. Реализации дренируют буфер pending-команд платформенного слоя.
 */
interface CommandSource<in P : Player> : PlatformHandler<Player> {
    fun pollCommands(player: P): List<String>
}
