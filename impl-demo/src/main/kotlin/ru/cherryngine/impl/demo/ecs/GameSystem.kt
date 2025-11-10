package ru.cherryngine.impl.demo.ecs

interface GameSystem {
    fun tick(tickIndex: Long, tickStartMs: Long)
}