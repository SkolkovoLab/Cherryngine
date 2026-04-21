package ru.cherryngine.platform.minecraft.java.events

import net.minestom.server.network.player.GameProfile
import ru.cherryngine.platform.minecraft.java.network.Connection

data class SetGameProfileEvent(
    val connection: Connection,
    val helloGameProfile: GameProfile,
    val onlineGameProfile: GameProfile?,
    var gameProfile: GameProfile,
)
