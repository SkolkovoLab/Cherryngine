package ru.cherryngine.impl.demo

import io.github.dockyardmc.DockyardServer
import jakarta.inject.Singleton

@Singleton
class DemoInit(
    dockyardServer: DockyardServer,
    testPacketHandler: TestPacketHandler,
) {
    init {
        dockyardServer.start(testPacketHandler)
    }
}