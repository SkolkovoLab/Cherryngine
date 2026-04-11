package ru.cherryngine.engine.minecraft.systems

import com.github.quillraven.fleks.IntervalSystem
import ru.cherryngine.engine.minecraft.entity.McEntityRegistry

class McEntityEndTickSystem(
    private val registry: McEntityRegistry,
) : IntervalSystem() {
    override fun onTick() {
        registry.endTick { }
    }
}
