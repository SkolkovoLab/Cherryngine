package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.scheduler.GlobalRegionScheduler
import ac.grim.grimac.platform.api.scheduler.TaskHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GlobalRegionSchedulerImpl : GlobalRegionScheduler {
    override fun execute(
        plugin: GrimPlugin,
        task: Runnable,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            task.run()
        }
    }

    override fun run(
        plugin: GrimPlugin,
        task: Runnable,
    ): TaskHandle {
        val job = CoroutineScope(Dispatchers.IO).launch {
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
        val job = CoroutineScope(Dispatchers.IO).launch {
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
        val job = CoroutineScope(Dispatchers.IO).launch {
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