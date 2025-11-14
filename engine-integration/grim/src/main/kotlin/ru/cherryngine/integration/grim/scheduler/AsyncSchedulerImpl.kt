package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.scheduler.AsyncScheduler
import ac.grim.grimac.platform.api.scheduler.TaskHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsyncSchedulerImpl : AsyncScheduler {
    override fun runNow(
        plugin: GrimPlugin,
        task: Runnable,
    ): TaskHandle {
        val job = CoroutineScope(Dispatchers.IO).launch {
            task.run()
        }
        return JobTaskHandle(job, false)
    }

    override fun runDelayed(
        plugin: GrimPlugin,
        task: Runnable,
        delay: Long,
        timeUnit: TimeUnit,
    ): TaskHandle {
        val delayMs = timeUnit.toMillis(delay)
        val job = CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            task.run()
        }
        return JobTaskHandle(job, false)
    }

    override fun runAtFixedRate(
        plugin: GrimPlugin,
        task: Runnable,
        delay: Long,
        period: Long,
        timeUnit: TimeUnit,
    ): TaskHandle {
        val delayMs = timeUnit.toMillis(delay)
        val periodMs = timeUnit.toMillis(period)
        val job = CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            while (true) {
                task.run()
                delay(periodMs)
            }
        }
        return JobTaskHandle(job, false)
    }

    override fun runAtFixedRate(
        plugin: GrimPlugin,
        task: Runnable,
        initialDelayTicks: Long,
        periodTicks: Long,
    ): TaskHandle {
        val delayMs = initialDelayTicks * 50
        val periodMs = periodTicks * 50
        val job = CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            while (true) {
                task.run()
                delay(periodMs)
            }
        }
        return JobTaskHandle(job, false)
    }

    override fun cancel(plugin: GrimPlugin) = Unit
}