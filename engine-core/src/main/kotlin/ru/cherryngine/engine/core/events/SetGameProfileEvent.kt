package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.server.Connection

data class SetGameProfileEvent(
    val connection: Connection,
    val helloGameProfile: GameProfile,
    val onlineGameProfile: GameProfile?,
    var gameProfile: GameProfile,
)