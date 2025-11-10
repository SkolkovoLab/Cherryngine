package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.components.EventsComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundChatCommandPacket

class CommandSystem() : IteratingSystem(
    family { all(EventsComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        val packets = entity[EventsComponent][PacketsEvent::class]?.packets ?: return
        packets.forEach { packet ->
            if (packet is ServerboundChatCommandPacket) {
                onCommand(entity, packet.command)
            }
        }
    }

    fun onCommand(entity: Entity, command: String) {
        val playerComponent = entity.getOrNull(PlayerComponent) ?: return
        val split = command.split(" ")

        when (split.getOrNull(0)) {
            "world" -> {
                val viewContextID = split[1]
                playerComponent.viewContextID = viewContextID
                entity.getOrNull(ViewableComponent)?.viewContextID = viewContextID
            }
        }
    }
}