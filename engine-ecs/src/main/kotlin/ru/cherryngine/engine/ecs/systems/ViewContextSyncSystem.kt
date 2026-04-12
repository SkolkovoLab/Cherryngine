package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.services.WorldService
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.components.PlayerComponent

class ViewContextSyncSystem(
    private val worldService: WorldService,
    private val playerManager: PlayerManager,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val playerComponent = entity[PlayerComponent]
        val player = playerManager.getPlayerNullable(playerComponent.uuid) ?: return
        worldService.setPlayerContext(playerComponent.viewContextIDs, player)
    }
}
