package ru.cherryngine.engine.core.instance

annotation class TickablePriority(
    val stage: Int = TickStage.GAME,
)
