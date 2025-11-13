package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent

class PacketLogSystem() : IteratingSystem(
    family { all(PacketsEvent) }
) {
    override fun onTickEntity(entity: Entity) {
        val packets = entity[PacketsEvent].packets
        println(packets)
    }
}