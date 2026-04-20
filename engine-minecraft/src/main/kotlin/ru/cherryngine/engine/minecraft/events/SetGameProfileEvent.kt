package ru.cherryngine.engine.minecraft.events

import net.minestom.server.network.player.GameProfile
import ru.cherryngine.lib.minecraft.network.Connection

data class SetGameProfileEvent(
    val connection: Connection,
    val helloGameProfile: GameProfile,
    val onlineGameProfile: GameProfile?,
    var gameProfile: GameProfile,
)
