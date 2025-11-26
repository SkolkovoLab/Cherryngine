package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.engine.ecs.EcsEntity
import ru.cherryngine.engine.ecs.events.PacketsEvent

class PacketLogSystem() : IteratingSystem(
    family { all(PacketsEvent) }
) {
    override fun onTickEntity(entity: EcsEntity) {
        val packets = entity[PacketsEvent].packets
        println(packets)
    }
}