package ru.cherryngine.engine.ecs.events

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket

class PacketsEvent(
    val packets: List<ServerboundPacket>,
) : EcsEvent<PacketsEvent> {
    override fun type() = PacketsEvent

    companion object : ComponentType<PacketsEvent>()
}