package ru.cherryngine.engine.core

import kotlin.time.Duration

interface Tickable {
    fun tick(delta: Duration)
}
