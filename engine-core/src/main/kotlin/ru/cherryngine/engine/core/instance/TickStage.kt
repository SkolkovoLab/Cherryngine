package ru.cherryngine.engine.core.instance

object TickStage {
    /** Снепшот сетевого ввода в per-tick read-only state. Должен идти ДО PRE-потребителей. */
    const val INPUT = -2
    const val PRE = -1
    const val GAME = 0
    const val POST = 1
}
