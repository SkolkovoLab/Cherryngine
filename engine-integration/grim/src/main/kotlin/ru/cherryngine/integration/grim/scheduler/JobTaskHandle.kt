package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.platform.api.scheduler.TaskHandle
import kotlinx.coroutines.Job

class JobTaskHandle(
    private val job: Job,
    private val isSync: Boolean,
) : TaskHandle {
    override fun isSync(): Boolean = isSync
    override fun isCancelled(): Boolean = job.isCancelled
    override fun cancel() = job.cancel()
}