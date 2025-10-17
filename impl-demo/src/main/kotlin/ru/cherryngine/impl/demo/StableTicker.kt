package ru.cherryngine.impl.demo

import kotlin.time.Duration

class StableTicker(
    val tickDuration: Duration,
    val tickFunction: (tickIndex: Long, tickStartMs: Long) -> Unit,
) {
    val tickMs = tickDuration.inWholeMilliseconds
    private var thread: Thread? = null

    private var started = false

    private val startTime: Long = System.currentTimeMillis()
    private var tickCounter: Long = 0

    fun start() {
        if (started) throw IllegalStateException("Already started")
        started = true
        val thread = Thread {
            while (started) {
                val tickStart: Long = System.currentTimeMillis()
                tickFunction(tickCounter, tickStart)
                tickCounter++
                val tickEnd: Long = System.currentTimeMillis()
                val tickTime = (tickEnd - tickStart).toInt()

                val nextTickStart = startTime + (tickCounter * tickMs)

                val sleepTime: Long = nextTickStart - tickEnd
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime)
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            }
        }
        thread.start()
        this.thread = thread
    }

    fun stop() {
        if (!started) throw IllegalStateException("Already stopped")
        started = false
        this.thread = null
    }
}