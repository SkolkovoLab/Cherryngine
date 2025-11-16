package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.scheduler.GlobalRegionScheduler
import ac.grim.grimac.platform.api.scheduler.TaskHandle
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.lang.Runnable

class GlobalRegionSchedulerImpl : GlobalRegionScheduler {
    private val logger = LoggerFactory.getLogger(GlobalRegionSchedulerImpl::class.java)
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        logger.error(e.message, e)
    }
    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    override fun execute(
        plugin: GrimPlugin,
        task: Runnable,
    ) {
        scope.launch {
            task.run()
        }
    }

    override fun run(
        plugin: GrimPlugin,
        task: Runnable,
    ): TaskHandle {
        val job = scope.launch {
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun runDelayed(
        plugin: GrimPlugin,
        task: Runnable,
        delay: Long,
    ): TaskHandle {
        val delayMs = delay * 50
        val job = scope.launch {
            delay(delayMs)
            task.run()
        }
        return JobTaskHandle(job, true)
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
        return JobTaskHandle(job, true)
    }

    override fun cancel(plugin: GrimPlugin) = Unit
}