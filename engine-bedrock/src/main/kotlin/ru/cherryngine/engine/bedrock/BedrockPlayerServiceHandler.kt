package ru.cherryngine.engine.bedrock

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.services.PlayerServiceHandler

@Singleton
class BedrockPlayerServiceHandler : PlayerServiceHandler {
    override fun canHandle(player: Player) = player is BedrockPlayer
    override fun onPlayerJoin(player: Player) {}
    override fun onPlayerLeave(player: Player) {}
}
