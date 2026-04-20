package ru.cherryngine.integration.grim.player

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory
import ac.grim.grimac.platform.api.sender.SenderFactory
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.lib.minecraft.network.Connection
import java.util.UUID

@Singleton
class PlatformPlayerFactoryImpl(
    private val playerManager: PlayerManager,
    private val senderFactory: SenderFactory<CommandSender>,
) : PlatformPlayerFactory {
    override fun getOfflineFromUUID(uuid: UUID): OfflinePlatformPlayer? = getFromUUID(uuid)

    override fun getOfflineFromName(name: String): OfflinePlatformPlayer? = getFromName(name)

    override fun getFromName(name: String): PlatformPlayer? {
        val player = playerManager.getPlayerNullable(name) as? MinecraftPlayer ?: return null
        return PlatformPlayerImpl(player, senderFactory)
    }

    override fun getFromUUID(uuid: UUID): PlatformPlayer? {
        val player = playerManager.getPlayerNullable(uuid) as? MinecraftPlayer ?: return null
        return PlatformPlayerImpl(player, senderFactory)
    }

    override fun getFromNativePlayerType(playerObject: Any): PlatformPlayer {
        val connection = playerObject as Connection
        val player = playerManager.getPlayerNullable(connection.gameProfile.uuid()) as? MinecraftPlayer
            ?: error("Player for connection ${connection.gameProfile.name()} not found")
        return PlatformPlayerImpl(player, senderFactory)
    }

    override fun invalidatePlayer(uuid: UUID) = Unit

    override fun getOnlinePlayers(): Collection<PlatformPlayer> {
        return playerManager.onlinePlayers().filterIsInstance<MinecraftPlayer>()
            .map { PlatformPlayerImpl(it, senderFactory) }
    }
}
