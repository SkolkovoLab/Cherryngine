package ru.cherryngine.engine.core.platform

import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.platform.PlatformHandler
import java.util.UUID

interface EntityRenderer<in P : Player> : PlatformHandler<Player> {
    fun onAdd(id: UUID)
    fun onRemove(id: UUID)
    fun show(id: UUID, player: P)
    fun hide(id: UUID, player: P)
}
