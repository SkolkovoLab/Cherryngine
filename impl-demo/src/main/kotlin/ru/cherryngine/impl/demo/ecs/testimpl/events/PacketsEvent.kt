package ru.cherryngine.impl.demo.ecs.testimpl.events

import ru.cherryngine.impl.demo.ecs.GameEvent
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket

data class PacketsEvent(
    val packets: List<ServerboundPacket>,
) : GameEvent