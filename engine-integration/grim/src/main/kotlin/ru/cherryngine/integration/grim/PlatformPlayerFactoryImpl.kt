package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory
import ru.cherryngine.engine.core.Player
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*

class PlatformPlayerFactoryImpl : PlatformPlayerFactory {
    override fun getOfflineFromUUID(uuid: UUID?): OfflinePlatformPlayer {
        TODO("Not yet implemented")
    }

    override fun getOfflineFromName(name: String?): OfflinePlatformPlayer {
        TODO("Not yet implemented")
    }

    override fun getFromName(name: String?): PlatformPlayer {
        TODO("Not yet implemented")
    }

    override fun getFromUUID(uuid: UUID?): PlatformPlayer {
        TODO("Not yet implemented")
    }

    override fun getFromNativePlayerType(playerObject: Any): PlatformPlayer {
        return PlatformPlayerImpl(playerObject as Player)
    }

    override fun invalidatePlayer(uuid: UUID?) {
        TODO("Not yet implemented")
    }

    override fun getOnlinePlayers(): Collection<PlatformPlayer> {
        TODO("Not yet implemented")
    }
}