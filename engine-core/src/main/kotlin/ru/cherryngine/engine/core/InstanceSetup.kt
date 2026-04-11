package ru.cherryngine.engine.core

interface InstanceSetupFactory<T : InstanceSetup> {
    fun create(): T
}

interface InstanceSetup {
    fun createTickables(): List<Tickable>
}
