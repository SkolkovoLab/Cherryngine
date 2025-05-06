package ru.cherryngine.engine.core

import io.github.dockyardmc.DockyardServer
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class DockyardModule(
    private val engineCoreConfig: EngineCoreConfig,
) {
    @Singleton
    fun getDockyardServer(): DockyardServer {
        return DockyardServer {
            ip = engineCoreConfig.address
            port = engineCoreConfig.port
            useMojangAuth = engineCoreConfig.mojangAuth
            networkCompressionThreshold = engineCoreConfig.compressionThreshold
        }
    }
}