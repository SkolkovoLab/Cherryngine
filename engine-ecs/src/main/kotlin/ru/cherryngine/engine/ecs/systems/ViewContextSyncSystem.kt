package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.WorldService
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent

class ViewContextSyncSystem(
    private val worldService: WorldService,
    private val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val uuid = entity[PlayerComponent].uuid
        val contextIDs = entity[PlayerComponent].viewContextIDs
        val player = playerManager.getPlayerNullable(uuid) ?: return
        worldService.setPlayerContext(uuid, contextIDs, player)
    }
}
