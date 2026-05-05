package ru.cherryngine.platform.minecraft.java.player

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.instance.TickStage
import ru.cherryngine.engine.core.instance.Tickable
import ru.cherryngine.engine.core.player.PlayerManager
import kotlin.time.Duration

/**
 * В стадии INPUT (раньше PRE) перекладывает входящие пакеты каждого игрока в tickPackets-снепшот.
 * Все Source'ы и Tickable'ы PRE/GAME/POST читают из снепшота через player.packets&lt;T&gt;().
 */
@InstanceSingleton(platform = "minecraft", stage = TickStage.INPUT)
class MinecraftInputSnapshotTickable(
    private val playerManager: PlayerManager,
) : Tickable {
    override fun tick(delta: Duration) {
        playerManager.onlinePlayers()
            .filterIsInstance<MinecraftPlayer>()
            .forEach { it.snapshotPackets() }
    }
}
