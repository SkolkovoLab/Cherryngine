package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.network.Connection
import ru.cherryngine.lib.minecraft.network.protocol.types.GameProfile

data class SetGameProfileEvent(
    val connection: Connection,
    val helloGameProfile: GameProfile,
    val onlineGameProfile: GameProfile?,
    var gameProfile: GameProfile,
)