package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.DockyardServer
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry

@Singleton
class DemoInit(
    dockyardServer: DockyardServer,
    testPacketHandler: TestPacketHandler,
) {
    init {
        dockyardServer.start(testPacketHandler)
    }
}