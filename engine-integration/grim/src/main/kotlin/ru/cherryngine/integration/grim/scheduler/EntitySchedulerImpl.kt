package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.entity.GrimEntity
import ac.grim.grimac.platform.api.scheduler.EntityScheduler
import ac.grim.grimac.platform.api.scheduler.TaskHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EntitySchedulerImpl : EntityScheduler{
    override fun execute(
        entity: GrimEntity,
        plugin: GrimPlugin,
        run: Runnable,
        retired: Runnable?,
        delay: Long,
    ) {
        val delayMs = delay * 50
        CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            run.run()
        }
    }

    override fun run(
        entity: GrimEntity,
        plugin: GrimPlugin,
        task: Runnable,
        retired: Runnable?,
    ): TaskHandle {
        val job = CoroutineScope(Dispatchers.IO).launch {
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun runDelayed(
        entity: GrimEntity,
        plugin: GrimPlugin,
        task: Runnable,
        retired: Runnable?,
        delayTicks: Long,
    ): TaskHandle {
        val delayMs = delayTicks * 50
        val job = CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun runAtFixedRate(
        entity: GrimEntity,
        plugin: GrimPlugin,
        task: Runnable,
        retired: Runnable?,
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
}