package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*

class PlatformPlayerFactoryImpl(
    private val playerManager: PlayerManager,
) : PlatformPlayerFactory {
    override fun getOfflineFromUUID(uuid: UUID): OfflinePlatformPlayer? {
        return getFromUUID(uuid)
    }

    override fun getOfflineFromName(name: String): OfflinePlatformPlayer? {
        return getFromName(name)
    }

    override fun getFromName(name: String): PlatformPlayer? {
        val player = playerManager.getPlayerNullable(name) ?: return null
        return PlatformPlayerImpl(player)
    }

    override fun getFromUUID(uuid: UUID): PlatformPlayer? {
        val player = playerManager.getPlayerNullable(uuid) ?: return null
        return PlatformPlayerImpl(player)
    }

    override fun getFromNativePlayerType(playerObject: Any): PlatformPlayer {
        val player = playerManager.getPlayer(playerObject as Connection)
        return PlatformPlayerImpl(player)
    }

    override fun invalidatePlayer(uuid: UUID) = Unit

    override fun getOnlinePlayers(): Collection<PlatformPlayer> {
        return playerManager.onlinePlayers().map { PlatformPlayerImpl(it) }
    }
}