package ru.cherryngine.engine.ecs.systems.mc_entity

import com.github.quillraven.fleks.IntervalSystem
import ru.cherryngine.engine.ecs.systems.mc_entity.McEntityRegistry

class McEntityEndTickSystem(
    private val registry: McEntityRegistry,
) : IntervalSystem() {
    override fun onTick() {
        registry.endTick { }
    }
}
