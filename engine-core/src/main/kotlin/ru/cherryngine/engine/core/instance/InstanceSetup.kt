package ru.cherryngine.engine.core.instance

interface InstanceSetupFactory<T : InstanceSetup>

interface InstanceSetup {
    fun createTickables(): List<Tickable>
}
