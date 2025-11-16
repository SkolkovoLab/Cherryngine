package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.scheduler.AsyncScheduler
import ac.grim.grimac.platform.api.scheduler.TaskHandle
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.lang.Runnable
import java.util.concurrent.TimeUnit

class AsyncSchedulerImpl : AsyncScheduler {
    private val logger = LoggerFactory.getLogger(AsyncSchedulerImpl::class.java)
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        logger.error(e.message, e)
    }
    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    override fun runNow(
        plugin: GrimPlugin,
        task: Runnable,
    ): TaskHandle {
        val job = scope.launch {
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
        val job = scope.launch {
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
        val job = scope.launch {
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
        val job = scope.launch {
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