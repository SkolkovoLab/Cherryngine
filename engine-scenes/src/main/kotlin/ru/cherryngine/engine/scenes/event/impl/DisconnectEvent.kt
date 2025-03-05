package ru.cherryngine.engine.scenes.event.impl

import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.scenes.event.Event

data class DisconnectEvent(
    val clientConnection: ClientConnection
) : Event