package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.components.ApartComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundChatCommandPacket
import java.util.*

class CommandSystem() : IteratingSystem(
    family { all(PacketsEvent) }
) {
    override fun onTickEntity(entity: Entity) {
        val packets = entity[PacketsEvent].packets
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
            "apart" -> {
                val apartId = split[1]
                if (apartId == "null") {
                    entity.configure {
                        it -= ApartComponent
                    }
                } else {
                    entity.configure {
                        it.getOrAdd(ApartComponent) { ApartComponent("") }.apartName = apartId
                    }
                }
            }

            "swap" -> {
                val otherUuid = UUID.fromString(split[1])
                val otherPlayer = world.family { all(PlayerComponent) }.firstOrNull {
                    it[PlayerComponent].uuid == otherUuid
                }
                if (otherPlayer != null) {
                    val tmp = playerComponent.uuid
                    playerComponent.uuid = otherPlayer[PlayerComponent].uuid
                    otherPlayer[PlayerComponent].uuid = tmp
                }
            }
        }
    }
}