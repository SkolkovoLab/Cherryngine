package ru.cherryngine.engine.minecraft

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.ModelServiceHandler
import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer

@Singleton
class MinecraftModelServiceHandler : ModelServiceHandler {
    override fun canHandle(player: Player) = player is MinecraftPlayer
    override fun onPlayerJoin(player: Player) {}
    override fun onPlayerLeave(player: Player) {}
}
