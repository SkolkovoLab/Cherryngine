package ru.cherryngine.integration.grim.player

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory
import ac.grim.grimac.platform.api.sender.SenderFactory
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*

@Singleton
class PlatformPlayerFactoryImpl(
    private val playerManager: PlayerManager,
    private val senderFactory: SenderFactory<CommandSender>,
) : PlatformPlayerFactory {
    override fun getOfflineFromUUID(uuid: UUID): OfflinePlatformPlayer? {
        return getFromUUID(uuid)
    }

    override fun getOfflineFromName(name: String): OfflinePlatformPlayer? {
        return getFromName(name)
    }

    override fun getFromName(name: String): PlatformPlayer? {
        val player = playerManager.getPlayerNullable(name) ?: return null
        return PlatformPlayerImpl(player, senderFactory)
    }

    override fun getFromUUID(uuid: UUID): PlatformPlayer? {
        val player = playerManager.getPlayerNullable(uuid) ?: return null
        return PlatformPlayerImpl(player, senderFactory)
    }

    override fun getFromNativePlayerType(playerObject: Any): PlatformPlayer {
        val player = playerManager.getPlayer(playerObject as Connection)
        return PlatformPlayerImpl(player, senderFactory)
    }

    override fun invalidatePlayer(uuid: UUID) = Unit

    override fun getOnlinePlayers(): Collection<PlatformPlayer> {
        return playerManager.onlinePlayers().map { PlatformPlayerImpl(it, senderFactory) }
    }
}