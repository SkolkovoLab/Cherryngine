package ru.cherryngine.impl.demo.ecs.testimpl.events

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket

class PacketsEvent(
    val packets: List<ServerboundPacket>,
) : Component<PacketsEvent> {
    override fun type() = PacketsEvent

    companion object : ComponentType<PacketsEvent>()
}