package ru.cherryngine.engine.ecs.systems

import com.github.quillraven.fleks.IntervalSystem
import ru.cherryngine.engine.ecs.EcsWorld

class CommandActionsSystem : IntervalSystem() {
    private val actions = mutableListOf<EcsWorld.() -> Unit>()

    fun addAction(action: EcsWorld.() -> Unit) {
        actions += action
    }

    override fun onTick() {
        actions.forEach { action -> world.action() }
        actions.clear()
    }

    companion object {
        fun EcsWorld.commandAction(action: EcsWorld.() -> Unit) {
            system<CommandActionsSystem>().addAction(action)
        }
    }
}