package ru.cherryngine.engine.ecs.systems.mc_entity

import com.github.quillraven.fleks.IntervalSystem
import ru.cherryngine.engine.minecraft.entity.McEntityRegistry

class McEntityBeginTickSystem(
    private val registry: McEntityRegistry,
) : IntervalSystem() {
    override fun onTick() {
        registry.beginTick()
    }
}
