package ru.cherryngine.engine.core

import io.github.dockyardmc.DockyardServer
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton

@Singleton
class ServerRunner(
    private val dockyardServer: DockyardServer,
) : ApplicationEventListener<StartupEvent> {
    override fun onApplicationEvent(event: StartupEvent) {
        dockyardServer.start()
    }
}