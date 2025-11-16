package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.scheduler.RegionScheduler
import ac.grim.grimac.platform.api.scheduler.TaskHandle
import ac.grim.grimac.platform.api.world.PlatformWorld
import ac.grim.grimac.utils.math.Location
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.lang.Runnable

class RegionSchedulerImpl : RegionScheduler {
    private val logger = LoggerFactory.getLogger(GlobalRegionSchedulerImpl::class.java)
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        logger.error(e.message, e)
    }
    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    override fun execute(
        plugin: GrimPlugin,
        world: PlatformWorld,
        chunkX: Int,
        chunkZ: Int,
        task: Runnable,
    ) {
        scope.launch {
            task.run()
        }
    }

    override fun execute(
        plugin: GrimPlugin,
        location: Location,
        task: Runnable,
    ) {
        scope.launch {
            task.run()
        }
    }

    override fun run(
        plugin: GrimPlugin,
        world: PlatformWorld,
        chunkX: Int,
        chunkZ: Int,
        task: Runnable,
    ): TaskHandle {
        val job = scope.launch {
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun run(
        plugin: GrimPlugin,
        location: Location,
        task: Runnable,
    ): TaskHandle {
        val job = scope.launch {
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun runDelayed(
        plugin: GrimPlugin,
        world: PlatformWorld,
        chunkX: Int,
        chunkZ: Int,
        task: Runnable,
        delayTicks: Long,
    ): TaskHandle {
        val delayMs = delayTicks * 50
        val job = scope.launch {
            delay(delayMs)
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun runDelayed(
        plugin: GrimPlugin,
        location: Location,
        task: Runnable,
        delayTicks: Long,
    ): TaskHandle {
        val delayMs = delayTicks * 50
        val job = scope.launch {
            delay(delayMs)
            task.run()
        }
        return JobTaskHandle(job, true)
    }

    override fun runAtFixedRate(
        plugin: GrimPlugin,
        world: PlatformWorld,
        chunkX: Int,
        chunkZ: Int,
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

    override fun runAtFixedRate(
        plugin: GrimPlugin,
        location: Location,
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
}