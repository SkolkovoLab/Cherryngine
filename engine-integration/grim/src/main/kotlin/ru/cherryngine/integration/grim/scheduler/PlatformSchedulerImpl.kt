package ru.cherryngine.integration.grim.scheduler

import ac.grim.grimac.platform.api.scheduler.*

class PlatformSchedulerImpl : PlatformScheduler {
    private val asyncScheduler = AsyncSchedulerImpl()
    private val globalRegionScheduler = GlobalRegionSchedulerImpl()
    private val entityScheduler = EntitySchedulerImpl()
    private val regionScheduler = RegionSchedulerImpl()

    override fun getAsyncScheduler(): AsyncScheduler = asyncScheduler
    override fun getGlobalRegionScheduler(): GlobalRegionScheduler = globalRegionScheduler
    override fun getEntityScheduler(): EntityScheduler = entityScheduler
    override fun getRegionScheduler(): RegionScheduler = regionScheduler
}

