package ru.cherryngine.engine.bedrock

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.services.WorldServiceHandler
import java.util.*

@Singleton
class BedrockWorldServiceHandler : WorldServiceHandler {
    override fun canHandle(player: Player) = player is BedrockPlayer
    override fun setPlayerContext(uuid: UUID, contextIDs: Set<String>) {}
    override fun onPlayerJoin(player: Player) {}
    override fun onPlayerLeave(player: Player) {}
}
