package ru.cherryngine.engine.core.instance

import kotlin.time.Duration

interface Tickable {
    fun tick(delta: Duration)
}
