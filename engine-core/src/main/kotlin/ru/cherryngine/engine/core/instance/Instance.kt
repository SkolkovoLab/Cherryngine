package ru.cherryngine.engine.core.instance

import ru.cherryngine.engine.core.utils.StableTicker
import kotlin.time.Duration

class Instance(
    val tickDuration: Duration,
    val tickables: List<Tickable>,
) : AutoCloseable {
    private val ticker = StableTicker(tickDuration) { _, _ ->
        tickables.forEach { it.tick(tickDuration) }
    }

    fun start() = ticker.start()
    fun stop() = ticker.stop()
    override fun close() = stop()
}
