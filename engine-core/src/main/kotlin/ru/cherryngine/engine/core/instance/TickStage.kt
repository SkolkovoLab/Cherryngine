package ru.cherryngine.engine.core.instance

enum class TickStage {
    /** Снепшот сетевого ввода в per-tick read-only state. Должен идти ДО PRE-потребителей. */
    INPUT,
    PRE,
    GAME,
    POST,
}
