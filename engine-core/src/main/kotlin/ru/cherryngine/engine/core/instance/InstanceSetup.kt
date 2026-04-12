package ru.cherryngine.engine.core.instance

interface InstanceSetupFactory<T : InstanceSetup> {
    fun create(): T
}

interface InstanceSetup {
    fun createTickables(): List<Tickable>
}
