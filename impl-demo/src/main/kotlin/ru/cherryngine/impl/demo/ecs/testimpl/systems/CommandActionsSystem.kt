package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.IntervalSystem
import ru.cherryngine.impl.demo.ecs.FleksWorld

class CommandActionsSystem : IntervalSystem() {
    private val actions = mutableListOf<FleksWorld.() -> Unit>()

    fun addAction(action: FleksWorld.() -> Unit) {
        actions += action
    }

    override fun onTick() {
        actions.forEach { action -> world.action() }
        actions.clear()
    }
}