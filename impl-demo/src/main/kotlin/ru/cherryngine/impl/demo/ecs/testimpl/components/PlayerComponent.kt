package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*

data class PlayerComponent(
    val connection: Connection,
    val packets: List<ServerboundPacket>,
    val viewContextUUID: UUID,
) : GameComponent